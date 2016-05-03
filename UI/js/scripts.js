'use strict';

var uniqueId = function () {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random).toString();
};
var theMessage = function (text) {
    return {
        date: new Date(),
        author: user,
        messageText: text,
        isChanged: false,
        isDeleted: false,
        id: uniqueId()
    };
};
var user = 'alex';
var messageList = [];
var mainUrl = ' http://192.168.100.7:999/chat';
var token = 'TN11EN';
var isChangening = false;
var isConnected = false;



function postMessage(newMessage) {
    var body;
   
        body = createMessage(newMessage.messageText, newMessage.id, "");
    
    ajax('POST', mainUrl, JSON.stringify(body), function () {
        
    });
};

function deleteMessage(delMessage) {
    var body;
   
        body = createMessage(delMessage.messageText, delMessage.id, "was deleted");
  
        
    
    //ajax('DELETE', mainUrl, JSON.stringify(deleteMessage.id), function () {
    ajax('POST', mainUrl, JSON.stringify(body), function () {
    });
};

function putMessage(putingMessage) {
    var body;
    
        body = createMessage(putingMessage.messageText, putingMessage.id, "was edited");
    

    //ajax('PUT', mainUrl, JSON.stringify(body), function () {
    ajax('POST', mainUrl, JSON.stringify(body), function () {
    });
};

function getMessageHistory() {
    var url = mainUrl + '?token=' + token;
    ajax('GET', url, null, function(responseText) {
        var json = JSON.parse(responseText);
        if (!messageList) {
            messageList = [];
        }
        for (var i = 0; i < json.messages.length; i++) {
            var newM = true;
            for (var j = 0; j < messageList.length; j++)
            {
                if(messageList[j].id == json.messages[i].id)
                {
                    if (json.messages[i].isEdit == "was deleted") {
                        messageList[j].isDeleted = true;
                    }
                    if (json.messages[i].isEdit == "was edited") {
                        messageList[j].isChanged = true;
                        messageList[j].messageText = json.messages[i].text;
                    }
                    newM = false;
                }
            }
            if (newM) {
                var newMessage = theMessage(json.messages[i].text);
                newMessage.date.setTime(json.messages[i].timestamp);
                newMessage.author = json.messages[i].author;
                newMessage.id = json.messages[i].id;
                messageList.push(newMessage);
                if (json.messages[i].isEdit == "was deleted") {
                    newMessage.isDeleted = true;
                }
                if (json.messages[i].isEdit == "was edited") {
                    newMessage.isChanged = true;
                }
            }
        }
        token = json.token;
        if (!isConnected) {
            var errorServer = document.getElementsByClassName('errorIcon')[0];
            errorServer.innerHTML = '<a><img class="connectionError" src="error.png" alt="Connection problems"></a>';
        }
        else {
            var foo = document.getElementsByClassName('errorIcon')[0];
            while (foo.firstChild) foo.removeChild(foo.firstChild);
        }
        removeAllMessages();
        createAllMessages(messageList);
        removeAllMessages();
        createAllMessages(messageList);
    });
}

function loop() {
    if (!isChangening) {
        getMessageHistory();
    }
    setTimeout(loop, 10000);
}

function run() {
    var usernameButton = document.getElementsByClassName('usernameButton')[0];
    var messageButton = document.getElementsByClassName('enterMessage')[0];
    messageList = 


    usernameButton.addEventListener('click', onUsernameChange);
    messageButton.addEventListener('click', onMessageEnter);

    var centerPart = document.getElementsByClassName('centralPart')[0];
    centerPart.addEventListener('click', onMassegeClick);


    loop();
}

function createAllMessages(allTasks) {
    for (var i = 0; i < allTasks.length; i++)
        addMessage(allTasks[i]);
}

function removeAllMessages() {
    var foo = document.getElementsByClassName('centralPart')[0];
    var space = foo.firstChild;
    while (foo.firstChild) foo.removeChild(foo.firstChild);
    foo.appendChild(space);
}

function onMassegeClick(evtObj) {
    if(evtObj.type === 'click' && evtObj.target.classList.contains('deleteButton')){
        onDeleteButtonClick(evtObj);
    }
    if(evtObj.type === 'click' && evtObj.target.classList.contains('changeButton')){
        onChangeButtonClick(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('canselButton')) {
        onCanselButtonClick(evtObj);
    }
}

function onDeleteButtonClick(evtObj) {
    var divForButtons = evtObj.target.parentElement.parentElement;
    var divForText = divForButtons.previousElementSibling;
    
    var pForMessage = divForButtons.parentElement;
    var divForMessage = divForButtons.parentElement.parentElement;
    var id = divForMessage.attributes['message-id'].value;
    pForMessage.removeChild(divForText);
    pForMessage.removeChild(divForButtons);
    for (var i = 0; i < messageList.length; i++) {
        if (messageList[i].id != id)
            continue;
        messageList[i].isDeleted = true;
        messageList[i].text = "DELETED";
        deleteMessage(messageList[i]);
        break;
    }
    store(messageList);
}

function onChangeButtonClick(evtObj) {
    if (!isChangening) {
        isChangening = true;
        var divForButtons = evtObj.target.parentElement.parentElement;
        var pForMessage = divForButtons.parentElement;
        var divForMessage = divForButtons.parentElement.parentElement;

        var text = divForButtons.previousElementSibling;
        var newText = document.createElement('textarea');
        newText.id = 'newText';
        var canselA = document.createElement('a');
        var canselImage = document.createElement('img');
        canselImage.classList.add('canselButton');
        canselImage.setAttribute('src', 'x.png');

        var id = divForMessage.attributes['message-id'].value;
        
        for (var i = 0; i < messageList.length; i++) {
            if (messageList[i].id != id)
                continue;
            newText.value = messageList[i].messageText;
        }

        pForMessage.removeChild(text);
        pForMessage.removeChild(divForButtons);
        pForMessage.appendChild(newText);
        pForMessage.appendChild(divForButtons);
        divForButtons.appendChild(canselA);
        canselA.appendChild(canselImage);
    } else {
        isChangening = false;
        var divForButtons = evtObj.target.parentElement.parentElement;
        var divForMessage = divForButtons.parentElement.parentElement;
        var pForMessage = divForButtons.parentElement;
        var divForText = document.createElement('div');
        var text = divForButtons.previousElementSibling;
        var canselA = divForButtons.lastElementChild;
        var messageTextarea = document.getElementById('newText');
        pForMessage.removeChild(text);
        pForMessage.removeChild(divForButtons);
        pForMessage.appendChild(divForText);
        divForText.appendChild(document.createTextNode(messageTextarea.value));
        pForMessage.appendChild(divForButtons);
        divForMessage.classList.remove('yourMessage');
        divForMessage.classList.add('yourMessageChanged');
        divForButtons.lastElementChild;
        divForButtons.removeChild(canselA);
        var id = divForMessage.attributes['message-id'].value;

        for (var i = 0; i < messageList.length; i++) {
            if (messageList[i].id != id)
                continue;
            messageList[i].messageText = messageTextarea.value;
            messageList[i].isChanged = true;
            putMessage(messageList[i]);
            break;
        }
        store(messageList);
    }
}

function onCanselButtonClick(evtObj)
{
    isChangening = false;
    var divForButtons = evtObj.target.parentElement.parentElement;
    var divForMessage = divForButtons.parentElement.parentElement;
    var pForMessage = divForButtons.parentElement;
    var divForText = document.createElement('div');
    var text = divForButtons.previousElementSibling;
    var canselA = divForButtons.lastElementChild;
    var messageTextarea = document.getElementById('newText');
    pForMessage.removeChild(text);
    pForMessage.removeChild(divForButtons);
    pForMessage.appendChild(divForText);
    var id = divForMessage.attributes['message-id'].value;
    for (var i = 0; i < messageList.length; i++) {
        if (messageList[i].id != id)
            continue;
        divForText.appendChild(document.createTextNode(messageList[i].messageText));
    }
    
    pForMessage.appendChild(divForButtons);

    divForButtons.lastElementChild;
    divForButtons.removeChild(canselA);
}

function onUsernameChange(){
    var username = document.getElementById('username');
    user = username.value;
    getMessageHistory();
}

function onMessageEnter(){
    var newMessageText = document.getElementById('messageTextarea');
    var newMessage = theMessage(newMessageText.value);
    postMessage(newMessage);

    addMessage(newMessage);
    newMessageText.value = '';
    window.scrollTo(0, document.body.scrollHeight);
    if (newMessage.messageText) {
        messageList.push(newMessage);
    }
    //postMessage(newMessage);
    //store(messageList);
}

function addMessage(newMessage) {
    if(!newMessage.messageText){
        return;
    }
    if (getElementsByAttribute('message-id', newMessage.id).length > 0) {
        var messageInTheList;
        for (var i = 0; i < messageList.length; i++) {
            if (messageList[i].id == id) {
                messageInTheList = messageList[i];
                break;
            }
        }
        if (newMessage.text == "DELETED") {
                messageInTheList.isDeleted = true;
        } else {
            messageInTheList.isChanged = true;
            messageInTheList.messageText = newMessage.text;
        }
    } else {
        var item = createItem(newMessage);
        var items = document.getElementsByClassName('centralPart')[0];
        items.appendChild(item);
    }
}

function createItem(newMessage) {
    var divForMessage = document.createElement('div');
    var pForMessage = document.createElement('p');
    var divForTime = document.createElement('div');
    var divForButtons = document.createElement('div');
    var divForText = document.createElement('div');
    var deleteAnchor = document.createElement('a');
    var changeAnchor = document.createElement('a');
    var deleteImage = document.createElement('img');
    var changeImage = document.createElement('img');
    deleteImage.classList.add('deleteButton');
    changeImage.classList.add('changeButton');
    deleteImage.setAttribute('src','delete.png');
    changeImage.setAttribute('src','pen1.png');
    deleteAnchor.appendChild(deleteImage);
    changeAnchor.appendChild(changeImage);
    divForButtons.appendChild(deleteAnchor);
    divForButtons.appendChild(changeAnchor);
    var d = newMessage.date;
    divForTime.appendChild(document.createTextNode(d));
    divForMessage.appendChild(pForMessage);
    
    divForText.appendChild(document.createTextNode(newMessage.messageText));
    
    divForMessage.setAttribute('message-id', newMessage.id);
    if (newMessage.author == user) {
        pForMessage.appendChild(divForTime);
        pForMessage.appendChild(divForText);
        pForMessage.appendChild(divForButtons);
        divForMessage.classList.add('yourMessage');
        if (newMessage.isChanged) {
            divForMessage.classList.remove('yourMessage');
            divForMessage.classList.add('yourMessageChanged');
        }
        if (newMessage.isDeleted) {
            pForMessage.removeChild(divForText);
            pForMessage.removeChild(divForButtons);
        }
    } else {
        var divForUser = document.createElement('div');
        divForUser.appendChild(document.createTextNode('By: '+newMessage.author));
        pForMessage.appendChild(divForUser);
        pForMessage.appendChild(divForTime);
        pForMessage.appendChild(divForText);
        if (!newMessage.isDeleted && !newMessage.isChanged) {
            divForMessage.classList.add('someoneMessage');
        }
        if (newMessage.isChanged) {
            divForMessage.classList.add('messageChanged');
        }
        if (newMessage.isDeleted) {
            divForMessage.classList.add('messageDeleted');
            pForMessage.removeChild(divForText);
        }
    }


    return divForMessage;
}

function store(listToSave) {
    

    if (typeof (Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    localStorage.setItem("MessagesOkamiUser", user);
    localStorage.setItem("MessagesOkami", JSON.stringify(listToSave));
}

function restore() {
    if (typeof (Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    var item = localStorage.getItem("MessagesOkami");
    user = localStorage.getItem("MessagesOkamiUser");
    return item && JSON.parse(item);
}

function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4)
            return;

        if (xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }

        if (isError(xhr.responseText)) {
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }

        continueWith(xhr.responseText);
        isConnected = true;
    };

    xhr.ontimeout = function () {
        ServerError();
    }

    xhr.onerror = function (e) {
        ServerError();
    };

    xhr.send(data);
}

function defaultErrorHandler(message) {
    console.error(message);
    output(message);
}

function isError(text) {
    if (text == "")
        return false;

    try {
        var obj = JSON.parse(text);
    } catch (ex) {
        return true;
    }

    return !!obj.error;
}

function ServerError() {
    isConnected = false;    
}

var getElementsByAttribute = function (attr, value) {
    var match = [];

    /* Get the droids we're looking for*/
    var elements = document.getElementsByTagName("*");

    /* Loop through all elements */
    for (var ii = 0, ln = elements.length; ii < ln; ii++) {

        if (elements[ii].hasAttribute(attr)) {

            /* If a value was passed, make sure it matches the element's */
            if (value) {

                if (elements[ii].getAttribute(attr) === value) {
                    match.push(elements[ii]);
                }
            } else {
                /* Else, simply push it */
                match.push(elements[ii]);
            }
        }
    }
    return match;
};

function createMessage(msg, mesId, edit) {
    return {
        author: user,
        text: msg,
        id: mesId,
        timestamp: new Date().getTime(),
        isEdit: edit
    }
}