function doGet(e){
  var output;
  
  var action = e.parameters.action;
 
  if(action == 'upload'){
    output = submit(e);
  } else if (action == 'create'){
    output = create(e);
  } else if (action == 'hideUpdate'){
    saveShowUpdate(false);
    output  = "The update message will no longer appear in new spreadsheets.";
  } else if (action == 'test'){
    output = 'Browser test complete';
  } else {
    output = 'Invalid action code.';
  }
  
  return ContentService.createTextOutput(output);
}