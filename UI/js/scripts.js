function run(){
    var usernameButton = document.getElementsByClassName('usernameButton')[0];
    var messageButton = document.getElementsByClassName('enterMessage')[0];


    usernameButton.addEventListener('click', onUsernameChange);
    messageButton.addEventListener('click', onMessageEnter);

    var centerPart = document.getElementsByClassName('centralPart')[0];
    centerPart.addEventListener('click', onMassegeClick);

    window.scrollTo(0, document.body.scrollHeight);

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

function onDeleteButtonClick(evtObj){
    evtObj.target.parentElement.parentElement.parentElement.remove();
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
        //newText.value = text.firstElementChild.value;
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
    divForText.appendChild(document.createTextNode(messageTextarea.value));
    pForMessage.appendChild(divForButtons);

    divForButtons.lastElementChild;
    divForButtons.removeChild(canselA);
}


function onUsernameChange(){
    var username = document.getElementById('username');

}

function onMessageEnter(){
    var newMessage = document.getElementById('messageTextarea');

    addMessage(newMessage.value);
    newMessage.value = '';
    window.scrollTo(0, document.body.scrollHeight);

}

function addMessage(value) {
    if(!value){
        return;
    }
    var item = createItem(value);
    var items = document.getElementsByClassName('centralPart')[0];

    items.appendChild(item);
}

function createItem(text){
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
    var d = new Date();
    var t= d.getTime();
    divForTime.appendChild(document.createTextNode(d.toDateString()+" "+d.toTimeString()));
    divForMessage.appendChild(pForMessage);
    pForMessage.appendChild(divForTime);
    pForMessage.appendChild(divForText);
    divForText.appendChild(document.createTextNode(text));
    pForMessage.appendChild(divForButtons);
    divForMessage.classList.add('yourMessage');


    return divForMessage;
}