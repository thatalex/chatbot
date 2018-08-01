onRowSelect = (function onselect(url, rowId, selId) {
    var rows = document.getElementsByClassName('selectable_row');
    for (var i = rows.length - 1; i >= 0; --i) {
        rows[i].className = 'selectable_row';

    }
    var selRow = document.getElementById(rowId);
    if (selRow != null) {
        selRow.className += ' highlight';
    }
    var context = '/';
    /*[+
            context = [[@{/}]];
        +]*/

    var edit = document.getElementById("edit_element");
    if (edit != null) {
        edit.href = context + url +"?action=edit&id=" + selId;
    }
    var del = document.getElementById("delete_element");
    if (del != null) {
        del.action = context + url + "?action=delete&id=" + selId;
    }
});
