/**
 * TYPES
 */
declare type initialize = (baudrate: number) => Promise<any>;
declare type uninitialize = () => Promise<any>;
declare type setIsDebug = (isDebug: boolean) => void;
declare type gpioSetStateOne = (name: string, state: number) => Promise<any>;
declare type gpioSetDirectionOne = (name: string, dir: string) => Promise<any>;
declare type gpioReadStateAllOld = (gpioList: Array<any>) => Promise<any>;
declare type gpioReadStateAllNoReset = (promise: Promise<any>) => Promise<any>;
declare type gpioReadStateAll = (promise: Promise<any>) => Promise<any>;
declare type gpioReadStateOne = (gpioName: string) => Promise<any>;
declare type gpioGetVersion = () => Promise<any>;
declare type setKioscoMode = (activated: boolean) => Promise<any>;
/**
 * METHODS
 */
declare const initialize: initialize;
declare const uninitialize: uninitialize;
declare const setIsDebug: setIsDebug;
declare const gpioSetStateOne: gpioSetStateOne;
declare const gpioSetDirectionOne: gpioSetDirectionOne;
declare const gpioReadStateAllOld: gpioReadStateAllOld;
declare const gpioReadStateAllNoReset: gpioReadStateAllNoReset;
declare const gpioReadStateAll: gpioReadStateAll;
declare const gpioReadStateOne: gpioReadStateOne;
declare const gpioGetVersion: gpioGetVersion;
declare const setKioscoMode: setKioscoMode;
declare const _default: {
    initialize: initialize;
    uninitialize: uninitialize;
    setIsDebug: setIsDebug;
    gpioSetStateOne: gpioSetStateOne;
    gpioSetDirectionOne: gpioSetDirectionOne;
    gpioReadStateAllOld: gpioReadStateAllOld;
    gpioReadStateAllNoReset: gpioReadStateAllNoReset;
    gpioReadStateAll: gpioReadStateAll;
    gpioReadStateOne: gpioReadStateOne;
    gpioGetVersion: gpioGetVersion;
    setKioscoMode: setKioscoMode;
};
export default _default;
