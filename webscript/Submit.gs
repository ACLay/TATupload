function doGet(e){
  var output = '';
  
  var action = e.parameters.action;
 
  if(action == 'upload'){
    output = submit(e);
  } else if (action == 'create'){
    output = createForm(e);
  } else if (action == 'test'){
    output = 'Browser test complete';
  } else {
    output = 'Invalid action code.';
  }
  
  return ContentService.createTextOutput(output);
 
}