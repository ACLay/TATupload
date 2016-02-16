function upload(number, question, location, toastie, SMS, time){
  try{
    submitSMS(number, question, location, toastie, SMS, time);
    return 'Message submitted';
  }catch (err){
    return 'Unable to submit message. Error description: ' + err.message;
  }
}

function submitSMS(number, question, location, toastie, SMS, time){
  var sheet = findSheet();
  var row = [time, number, question, location, toastie, SMS];
  sheet.appendRow(row);//atomic operation
}

function findSheet(){
  var ss = SpreadsheetApp.openById(getSpreadsheetId());
  var sheets = ss.getSheets();
  var sheetId = getSheetId();
  for(var i = 0; i < sheets.length; i++){
    if(sheets[i].getSheetId() == sheetId){
      return sheets[i];
    }
  }
  //Throw error if sheet cannot be found
  throw {message:"Unable to find the sheet"};
}

function testSubmit(){
  try{
    submit("42", "which?", "somewhere", "cheese ham", "SMS thing, testing!", "tea time");
    return 'Message submitted';
  }catch (err){
    Logger.log(err.message);
    return 'Unable to submit message. Error description: ' + err.message;
  }
}