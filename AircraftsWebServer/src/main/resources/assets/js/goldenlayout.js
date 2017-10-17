var config = {
    content: [{
        type: 'row',
        content: [{
            type:'component',
            componentName: 'example',
            componentState: { text: 'Component 1' }
        },
        {
            type:'component',
            componentName: 'example',
            componentState: { text: 'Component 2' }
        }]
    }]
};

var myLayout = new window.GoldenLayout( config, $('#layoutContainer') );

myLayout.registerComponent( 'example', function( container, state ){
    container.getElement().html( '<h2>' + state.text + '</h2>');
});

myLayout.init();

$(window).resize(function() {
  myLayout.updateSize()
})
