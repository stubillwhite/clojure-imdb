
function findPerson () {
    var searchString = document.getElementById('searchBox').value
    $.getJSON('/person?name=' + searchString, function(data) { displayResultGrid(data); });
}

function displayResultGrid (data) {
    // document.getElementById('results').innerHTML = '<a href="test">foo</a>';

    console.log($.each(data, function(idx, item) {
        console.log('<a href="/person/' + item.id + '">' + item.name + "</a>");
        '<a href="/person/' + item.id + '">' + item.name + "</a>";
    }).join('<br>'));
    
    document.getElementById('results').innerHTML = $.each(data, function(idx, item) {
        console.log('<a href="/person/' + item.id + '">' + item.name + "</a>");
        '<a href="/person/' + item.id + '">' + item.name + "</a>";
    }).join('<br>');
}

$(document).ready(function() {
    $('.searchBox').keydown(function(event) {
        if (event.keyCode == 13) {
            document.getElementById('searchButton').click();
            return false;
         }
    });
});
