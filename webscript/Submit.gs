function doGet(e){
  var output;
  var action = e.parameters.action;
 
  if(action == 'upload'){
    output = submit(e);
  } else if (action == 'create'){
    output = create(e);
  } else if (action == 'test'){
    output = 'Browser test complete';
  } else {
    output = 'Invalid action code.';
  }
  
  return ContentService.createTextOutput(output);
}

//prevent typing the literal incorrectly in code
function getSheetName(){
  return "Texts";
}