export function replaceAt<T>(list: T[], index: number, update: (old: T) => T): T[] {
    const old = list[index];
    const ret = list.slice(0);
    ret[index] = update(old);
    return ret;
}
