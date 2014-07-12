function submit(e){
  var number = e.parameters.number;
  var question = e.parameters.question;
  var location = e.parameters.location;
  var toastie = e.parameters.toastie;
  var SMS = e.parameters.SMS;
  var time = e.parameters.time;
  
  try{
    submitSMS(number, question, location, toastie, SMS, time);
    return 'Message submitted';
  }catch (err){
    return 'Unable to submit message. Error description: ' + err.message;
  }
}

function submitSMS(number, question, location, toastie, SMS, time){
  var sheet = findSheet();
  var row = [time[0], number[0], question[0], location[0], toastie[0], SMS[0]];//de-array needed with e.parameter because...?
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
    submitSMS(["42"], ["which?"], ["somewhere"], ["cheese ham"], ["SMS thing, testing!"], ["tea time"]);
    return 'Message submitted';
  }catch (err){
    Logger.log(err.message);
    return 'Unable to submit message. Error description: ' + err.message;
  }
}