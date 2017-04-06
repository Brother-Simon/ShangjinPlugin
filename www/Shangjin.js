function Shangjin() {
}

Shangjin.prototype.share = function (share_text, duration, position, successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback, "Shangjin", "share", [{"share_text":share_text}]);
};

Shangjin.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.shangjin = new Shangjin();
  return window.plugins.shangjin;
};

cordova.addConstructor(Shangjin.install);