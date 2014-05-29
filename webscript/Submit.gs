function doGet(e){
  var output = '';
  
  var action = e.parameters.action;
 
  if(action == 'upload'){
    output = submit(e);
  } else if (action == 'create'){
    output = createForm(e);
  } else {
    output = 'Invalid action code.';
  }
  
  return ContentService.createTextOutput(output);
 
}