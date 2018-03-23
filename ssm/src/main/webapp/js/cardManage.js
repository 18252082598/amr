var wsUri = "ws://127.0.0.1:4649/com";
var output;
function init() {
    output = document.getElementById("roomNo");
    //testWebSocket();
}
function testWebSocket() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (evt) {
        onOpen(evt);
    };
    websocket.onclose = function (evt) {
        onClose(evt);
    };
    websocket.onmessage = function (evt) {
        onMessage(evt);
    };
    websocket.onerror = function (evt) {
        onError(evt);
    };
}
function onOpen(evt) {
    doSend("read");
    //writeToScreen("CONNECTED </br>");
    
}
function onClose(evt) {
    //writeToScreen("DISCONNECTED");
}
function onMessage(evt) {
    writeToScreen(evt.data);
}
function onError(evt) {
    //writeToScreen('<span style="color: red;">ERROR:</span> </br>' + evt.data);
}
function doSend(message) {
    //writeToScreen("SENT: " + message + "</br>");
    websocket.send(message);
}
function writeToScreen(message) {
    output.value = message;
    findUser();
}
window.addEventListener("load", init, false);