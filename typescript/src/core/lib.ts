
export class Scanner {
    private src: Array<string> | null;
    private line: Array<string> | null;

    setSource(str: string) {
        this.src = str.split("\n");
    }

    readLine() {
        if (this.src) {
            this.line = this.src.shift().split(' ');
        } else {
            this.line = readline().split(' ');
            // printDebug(this.line.join(" "));
        }        
    }

    nextInt(): number { return parseInt(this.line.shift()) }

    nextToken(): string { return this.line.shift() }
}

export const printDebug = (message: string) => console.error(message);

export function array_maxBy<T>(array: Array<T>, fn: (item: T) => number): T {
    return array.reduce((l: T, r: T) => fn(l) > fn(r) ? l : r);
}

export function array_minBy<T>(array: Array<T>, fn: (item: T) => number): T {
    return array.reduce((l: T, r: T) => fn(l) < fn(r) ? l : r);
}

export function array_sumBy<T>(array: Array<T>, fn: (e: any) => number): number {
    return array.reduce((p: number, c: any) => p + fn(c), 0);
}

export function array_countBy<T>(array: Array<T>, fn: (e: any) => boolean): number {
    return array.reduce((p: number, c: any) => p + (fn(c) ? 1 : 0), 0);
}
