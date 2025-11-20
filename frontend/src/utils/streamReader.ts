import store from "@/store/cache.ts";

export interface StreamOptions {
    url: string
    method?: 'GET' | 'POST'
    headers?: Record<string, string>
    body?: any
    onMessage: (message: string) => void
    onError?: (error: any) => void
    onFinish?: () => void
    debug?: boolean
}

export const readStream = async (options: StreamOptions) => {
    const {
        url,
        method = 'POST',
        headers = {},
        body,
        onMessage,
        onError,
        onFinish,
        debug = false
    } = options

    try {
        const response = await fetch(url, {
            method,
            headers: {
                'Accept': 'text/event-stream',
                'Content-Type': 'application/json',
                'authentication': store.state.user.token ?? '',
                ...headers
            },
            body: body ? JSON.stringify(body) : undefined,
        })

        if (!response.ok || !response.body) {
            throw new Error(`请求失败: ${response.status} ${response.statusText}`)
        }

        const reader = response.body.getReader()
        const decoder = new TextDecoder('utf-8')
        let buffer = ''

        while (true) {
            const { done, value } = await reader.read()
            if (done) break

            buffer += decoder.decode(value, { stream: true })

            let boundary = '\n\n'
            let index: number

            while ((index = buffer.indexOf(boundary)) !== -1) {
                const part = buffer.slice(0, index)
                buffer = buffer.slice(index + boundary.length)

                if (part.startsWith('data:')) {
                    const data = part.replace(/data:/g, '')
                    if (debug) console.debug('[stream message]', data)
                    onMessage(data)
                }
            }
        }


        onFinish?.()
    } catch (err) {
        if (debug) console.error('[stream error]', err)
        onError?.(err)
    }
}
