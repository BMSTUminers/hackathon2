/**
 * Created by fckm on 14.11.15.
 */

var DragManager = new function() {

    /**
    * составной объект для хранения информации о переносе:
    * {
    *   elem - элемент, на котором была зажата мышь
    *   avatar - аватар
    *   downX/downY - координаты, на которых был mousedown
    *   shiftX/shiftY - относительный сдвиг курсора от угла элемента
    * }
    */
    var dragObject = {};

    var self = this;

    function onMouseDown(e) {

        if (e.which != 1) return;

        var elem = e.target.closest('.dataSourceField');
        if (!elem) return;

        dragObject.elem = elem;

        // запомним, что элемент нажат на текущих координатах pageX/pageY
        dragObject.downX = e.pageX;
        dragObject.downY = e.pageY;

        return false;
    }

    function onMouseMove(e) {
        if (!dragObject.elem) return; // элемент не зажат

        if (!dragObject.avatar) { // если перенос не начат...
            var moveX = e.pageX - dragObject.downX;
            var moveY = e.pageY - dragObject.downY;

            // если мышь передвинулась в нажатом состоянии недостаточно далеко
            if (Math.abs(moveX) < 3 && Math.abs(moveY) < 3) {
                return;
            }

            // начинаем перенос
            dragObject.avatar = createAvatar(e); // создать аватар
            if (!dragObject.avatar) { // отмена переноса, нельзя "захватить" за эту часть элемента
                dragObject = {};
                return;
            }

            // аватар создан успешно
            // создать вспомогательные свойства shiftX/shiftY
            var coords = getCoords(dragObject.avatar);
            dragObject.shiftX = dragObject.downX - coords.left;
            dragObject.shiftY = dragObject.downY - coords.top;

            startDrag(e); // отобразить начало переноса
        }

        // отобразить перенос объекта при каждом движении мыши
        dragObject.avatar.style.left = e.pageX - dragObject.shiftX + 'px';
        dragObject.avatar.style.top = e.pageY - dragObject.shiftY + 'px';

        return false;
    }

    function onMouseUp(e) {
        if (dragObject.avatar) {
            // если перенос идет
            var myDraggedElem = {};
            myDraggedElem.old = document.oldElemPos;
            myDraggedElem.element = dragObject.elem;

            //проверяем, есть ли уже данный элемент в списке "перетаскиваемых"
            var contains = false;
            for (var i = 0; i < document.draggedElements.length; i++) {
                if (document.draggedElements[i].element == myDraggedElem.element)
                    contains = true;
            }

            if (!contains)
                document.draggedElements.push(myDraggedElem);

            finishDrag(e);
        }

        // перенос либо не начинался, либо завершился
        // в любом случае очистим "состояние переноса" dragObject
        dragObject = {};
    }

    function finishDrag(e) {
        var dropElem = findDroppable(e);

        if (!dropElem) {
            self.onDragCancel(dragObject);
        } else {
            self.onDragEnd(dragObject, dropElem);
        }
    }

    function createAvatar(e) {

        // запомнить старые свойства, чтобы вернуться к ним при отмене переноса
        var avatar = dragObject.elem;
        var old = {
            parent: avatar.parentNode,
            nextSibling: avatar.nextSibling,
            position: avatar.position || '',
            left: avatar.left || '',
            top: avatar.top || '',
            zIndex: avatar.zIndex || ''
        };

        document.oldElemPos = old;

        // функция для отмены переноса
        avatar.rollback = function() {
            old.parent.insertBefore(avatar, old.nextSibling);
            avatar.style.position = old.position;
            avatar.style.left = old.left;
            avatar.style.top = old.top;
            avatar.style.zIndex = old.zIndex
        };

        return avatar;
    }

    function startDrag(e) {
        var avatar = dragObject.avatar;

        // инициировать начало переноса
        document.body.appendChild(avatar);
        avatar.style.zIndex = 9999;
        avatar.style.position = 'absolute';
    }

    function findDroppable(event) {
        // спрячем переносимый элемент
        dragObject.avatar.hidden = true;

        // получить самый вложенный элемент под курсором мыши
        var elem = document.elementFromPoint(event.clientX, event.clientY);

        // показать переносимый элемент обратно
        dragObject.avatar.hidden = false;

        if (elem == null) {
            // такое возможно, если курсор мыши "вылетел" за границу окна
            return null;
        }

        return elem.closest('.droppable');
    }

    document.onmousemove = onMouseMove;
    document.onmouseup = onMouseUp;
    document.onmousedown = onMouseDown;

    this.onDragEnd = function(dragObject, dropElem) {

        dropElem.removeChild(dropElem.firstChild);
        dropElem.appendChild(dragObject.avatar);
        //dropElem.parentNode.insertBefore(dragObject.avatar, dropElem.nextSibling);
        //dropElem.parent.insertBefore(dragObject.avatar, dropElem.nextSibling);
        dragObject.avatar.style.position = "";
        dragObject.avatar.style.left = "";
        dragObject.avatar.style.top = "";
        dragObject.avatar.style.zIndex = "";

        document.chosenFields.push(dragObject.avatar.innerHTML);

        var chosenChartField = dragObject.avatar.parentNode.parentNode.firstChild.innerHTML;
        var chosenField = dragObject.avatar.innerHTML;
        //var datasetId = document.dataSet[document.chosenIndex].id;
        //var datasetName = document.dataSet[document.chosenIndex].name;

        //alert("You have chosen the '" + chosenField + "' field in the '" + document.dataSet[document.chosenIndex].name +"' dataset. " +
        //    "The field is " + chosenChartField);

        switch (document.chartMode) {
            case "pie":
                if (chosenChartField.trim() === "Y:")
                    document.pieVars.func = chosenField;
                else
                    document.pieVars.arg = chosenField;
                break;
            case "bar":
                if (chosenChartField.trim() === "Y:")
                    document.barVars.func = chosenField;
                else
                    document.barVars.arg = chosenField;
                break;
            case "line":
                if (chosenChartField.trim() === "Y:")
                    document.lineVars.func = chosenField;
                else
                    document.lineVars.arg = chosenField;
                break;
            case "map":
                if (chosenChartField.trim() === "Широта")
                    document.mapVars.lat = chosenField;
                else if (chosenChartField.trim() === "Долгота")
                    document.mapVars.lon = chosenField;
                else
                    document.mapVars.fields.push(chosenField);

                if (dragObject.avatar.parentNode.parentNode.rowIndex + 1 == dragObject.avatar.parentNode.parentNode.parentNode.children.length) {
                    require([
                            "dojo/dom-construct",
                        ],

                        function (domConstruct) {
                            domConstruct.create("tr",
                                {innerHTML: '<td> Информация </td>' + '<td class="droppable">' + '<span class="border">' + 'Перетащите элемент сюда' + '</span>' + '</td>'

                                },
                                "ConstructorTable", "last");
                        });
                }
        }

    };
    this.onDragCancel = function(dragObject) {};

};


function getCoords(elem) { // кроме IE8-
    var box = elem.getBoundingClientRect();

    return {
        top: box.top + pageYOffset,
        left: box.left + pageXOffset
    };

}