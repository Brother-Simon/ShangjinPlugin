function Shangjin() {
}

Shangjin.prototype.optionsBuilder = function () {

  // defaults
  var message = null;
  var duration = "short";
  var position = "center";
  var addPixelsY = 0;

  return {
    withMessage: function(m) {
      message = m.toString();
      return this;
    },

    withDuration: function(d) {
      duration = d.toString();
      return this;
    },

    withPosition: function(p) {
      position = p;
      return this;
    },

    withAddPixelsY: function(y) {
      addPixelsY = y;
      return this;
    },

    build: function() {
      return {
        message: message,
        duration: duration,
        position: position,
        addPixelsY: addPixelsY
      };
    }
  };
};


Shangjin.prototype.showWithOptions = function (options, successCallback, errorCallback) {
  options.duration = (options.duration === undefined ? 'long' : options.duration.toString());
  options.message = options.message.toString();
  cordova.exec(successCallback, errorCallback, "Shangjin", "show", [options]);
};

Shangjin.prototype.show = function (message, duration, position, successCallback, errorCallback) {
  this.showWithOptions(
      this.optionsBuilder()
          .withMessage(message)
          .withDuration(duration)
          .withPosition(position)
          .build(),
      successCallback,
      errorCallback);
};

Shangjin.prototype.showShortTop = function (message, successCallback, errorCallback) {
  this.show(message, "short", "top", successCallback, errorCallback);
};

Shangjin.prototype.showShortCenter = function (message, successCallback, errorCallback) {
  this.show(message, "short", "center", successCallback, errorCallback);
};

Shangjin.prototype.showShortBottom = function (message, successCallback, errorCallback) {
  this.show(message, "short", "bottom", successCallback, errorCallback);
};

Shangjin.prototype.showLongTop = function (message, successCallback, errorCallback) {
  this.show(message, "long", "top", successCallback, errorCallback);
};

Shangjin.prototype.showLongCenter = function (message, successCallback, errorCallback) {
  this.show(message, "long", "center", successCallback, errorCallback);
};

Shangjin.prototype.showLongBottom = function (message, successCallback, errorCallback) {
  this.show(message, "long", "bottom", successCallback, errorCallback);
};

Shangjin.prototype.hide = function (successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback, "Shangjin", "hide", []);
};

Shangjin.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.shangjin = new Shangjin();
  return window.plugins.shangjin;
};

cordova.addConstructor(Shangjin.install);