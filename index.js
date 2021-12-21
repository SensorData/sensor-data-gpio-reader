"use strict";
exports.__esModule = true;
var react_native_1 = require("react-native");
var GpioReaderModule = react_native_1.NativeModules.GpioReaderModule;
/**
 * METHODS
 */
var initialize = function (baudrate) { return GpioReaderModule.initialize(baudrate); };
var uninitialize = function () { return GpioReaderModule.uninitialize(); };
var setIsDebug = function (isDebug) { return GpioReaderModule.setIsDebug(isDebug); };
var gpioSetStateOne = function (name, state) { return GpioReaderModule.gpioSetStateOne(name, state); };
var gpioSetDirectionOne = function () { return GpioReaderModule.gpioSetDirectionOne(); };
var gpioReadStateAllOld = function (gpioList) { return GpioReaderModule.gpioReadStateAllOld(gpioList); };
var gpioReadStateAllNoReset = function () { return GpioReaderModule.gpioReadStateAllNoReset(); };
var gpioReadStateAll = function () { return GpioReaderModule.gpioReadStateAll(); };
var gpioReadStateOne = function (gpioName) { return GpioReaderModule.gpioReadStateOne(gpioName); };
var gpioGetVersion = function () { return GpioReaderModule.gpioGetVersion(); };
var setKioscoMode = function (activated) { return GpioReaderModule.setKioscoMode(activated); };
exports["default"] = {
    initialize: initialize,
    uninitialize: uninitialize,
    setIsDebug: setIsDebug,
    gpioSetStateOne: gpioSetStateOne,
    gpioSetDirectionOne: gpioSetDirectionOne,
    gpioReadStateAllOld: gpioReadStateAllOld,
    gpioReadStateAllNoReset: gpioReadStateAllNoReset,
    gpioReadStateAll: gpioReadStateAll,
    gpioReadStateOne: gpioReadStateOne,
    gpioGetVersion: gpioGetVersion,
    setKioscoMode: setKioscoMode
};
