var uniqueId = function () {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random).toString();
};

var theMessage = function (text) {
    return {
        date: Date(),
        author: user,
        messageText: text,
        isChanged: false,
        isDeleted: false,
        id: uniqueId()
    };
};

var user = 'alex';
var messageList = [];

function run() {
    var usernameButton = document.getElementsByClassName('usernameButton')[0];
    var messageButton = document.getElementsByClassName('enterMessage')[0];


    usernameButton.addEventListener('click', onUsernameChange);
    messageButton.addEventListener('click', onMessageEnter);

    var centerPart = document.getElementsByClassName('centralPart')[0];
    centerPart.addEventListener('click', onMassegeClick);

    window.scrollTo(0, document.body.scrollHeight);
   
    messageList = restore();

    createAllMessages(messageList);
    
}

function createAllMessages(allTasks) {
    for (var i = 0; i < allTasks.length; i++)
        addMessage(allTasks[i]);
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
    var divForMessage = evtObj.target.parentElement.parentElement.parentElement;
    divForMessage.remove();
    var id = divForMessage.attributes['message-id'].value;
    for (var i = 0; i < messageList.length; i++) {
        if (messageList[i].id != id)
            continue;
        messageList[i].isDeleted = true;
    }
    store(messageList);
}

var isChangening = false;

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
    store(messageList);
    var foo = document.getElementsByClassName('centralPart')[0];
    var space = foo.firstChild;
    while (foo.firstChild) foo.removeChild(foo.firstChild);
    foo.appendChild(space);
    messageList = restore();
    createAllMessages(messageList);
}

function onMessageEnter(){
    var newMessageText = document.getElementById('messageTextarea');
    var newMessage = theMessage(newMessageText.value);

    addMessage(newMessage);
    newMessageText.value = '';
    window.scrollTo(0, document.body.scrollHeight);
    if (newMessage.messageText) {
        messageList.push(newMessage);
    }
    store(messageList);
}

function addMessage(newMessage) {
    if(!newMessage.messageText){
        return;
    }
    var item = createItem(newMessage);
    var items = document.getElementsByClassName('centralPart')[0];

    
    items.appendChild(item);
    

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
    pForMessage.appendChild(divForTime);
    pForMessage.appendChild(divForText);
    divForText.appendChild(document.createTextNode(newMessage.messageText));
    
    divForMessage.setAttribute('message-id', newMessage.id);
    if (newMessage.author == user) {
        pForMessage.appendChild(divForButtons);
        divForMessage.classList.add('yourMessage');
        if (newMessage.isChanged) {
            divForMessage.classList.remove('yourMessage');
            divForMessage.classList.add('yourMessageChanged');
        }
        if (newMessage.isDeleted) {
            divForMessage.removeChild(divForButtons);
        }
    } else {
        if (!newMessage.isDeleted && !newMessage.isChanged) {
            divForMessage.classList.add('someoneMessage');
        }
        if (newMessage.isChanged) {
            divForMessage.classList.add('messageChanged');
        }
        if (newMessage.isDeleted) {
            divForMessage.classList.add('messageChanged');
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
