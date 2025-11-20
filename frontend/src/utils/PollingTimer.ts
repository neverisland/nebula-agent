export class PollingTimer {
    private intervalId: number | null = null;
    private readonly interval: number;
    private callback: () => Promise<boolean>;

    constructor(callback: () => Promise<boolean>, interval: number = 2000) {
        this.callback = callback;
        this.interval = interval;
    }

    start = () => {
        if (this.intervalId !== null) return;

        this.intervalId = window.setInterval(async () => {
            try {
                const shouldStop = await this.callback();
                if (shouldStop) {
                    this.stop();
                }
            } catch (err) {
                console.error('Polling error:', err);
            }
        }, this.interval);

        console.log('Polling started.');
    };

    stop = () => {
        if (this.intervalId !== null) {
            clearInterval(this.intervalId);
            this.intervalId = null;
            console.log('Polling stopped.');
        }
    };

    isRunning = (): boolean => {
        return this.intervalId !== null;
    };
}
