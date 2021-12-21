import { NativeModules } from 'react-native';

const { GpioReaderModule } = NativeModules;

/**
 * TYPES
 */

type initialize = (baudrate: number) => Promise<any>;
type uninitialize = () => Promise<any>;
type setIsDebug = (isDebug: boolean) => void;
type gpioSetStateOne = (name: string, state: number) => Promise<any>;
type gpioSetDirectionOne = (name: string, dir: string) => Promise<any>
type gpioReadStateAllOld = (gpioList: Array<any>) => Promise<any>;
type gpioReadStateAllNoReset = (promise: Promise<any>) => Promise<any>;
type gpioReadStateAll = (promise: Promise<any>) => Promise<any>;
type gpioReadStateOne = (gpioName: string) => Promise<any>;
type gpioGetVersion = () => Promise<any>;
type setKioscoMode = (activated: boolean) => Promise<any>;

/**
 * METHODS
 */

const initialize: initialize = (baudrate) => GpioReaderModule.initialize(baudrate);

const uninitialize: uninitialize = () => GpioReaderModule.uninitialize();

const setIsDebug: setIsDebug = (isDebug) => GpioReaderModule.setIsDebug(isDebug);

const gpioSetStateOne: gpioSetStateOne = (name, state) => GpioReaderModule.gpioSetStateOne(name, state);

const gpioSetDirectionOne: gpioSetDirectionOne = () => GpioReaderModule.gpioSetDirectionOne();

const gpioReadStateAllOld: gpioReadStateAllOld = (gpioList) => GpioReaderModule.gpioReadStateAllOld(gpioList);

const gpioReadStateAllNoReset: gpioReadStateAllNoReset = () => GpioReaderModule.gpioReadStateAllNoReset();

const gpioReadStateAll: gpioReadStateAll = () => GpioReaderModule.gpioReadStateAll();

const gpioReadStateOne: gpioReadStateOne = (gpioName) => GpioReaderModule.gpioReadStateOne(gpioName);

const gpioGetVersion: gpioGetVersion = () => GpioReaderModule.gpioGetVersion();

const setKioscoMode: setKioscoMode = (activated) => GpioReaderModule.setKioscoMode(activated);


export default {
  initialize,
  uninitialize,
  setIsDebug,
  gpioSetStateOne,
  gpioSetDirectionOne,
  gpioReadStateAllOld,
  gpioReadStateAllNoReset,
  gpioReadStateAll,
  gpioReadStateOne,
  gpioGetVersion,
  setKioscoMode
};
