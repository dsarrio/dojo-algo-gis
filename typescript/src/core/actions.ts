import { printDebug } from './lib';
import { Factory } from './models';

class Actions {
    private queue: string[] = [];

    message(message: string) {
        this.queue.push(`MSG ${message}`)
    }

    move(source: Factory, destination: Factory, nbCyborgs: number) {
        this.queue.push(`MOVE ${source.id} ${destination.id} ${nbCyborgs}`)
    }

    bomb(source: Factory, destination: Factory) {
        this.queue.push(`BOMB ${source.id} ${destination.id}`)
    }

    upgrade(factory: Factory) {
        this.queue.push(`INC ${factory.id}`)
    }

    commit() {
        if (this.queue.length == 0) {
            printDebug(`No action found /!\\`);
            this.queue.push('WAIT');
        }
        console.log(this.queue.join(';'));
        this.queue = [];
    }
}

export const actions = new Actions();
