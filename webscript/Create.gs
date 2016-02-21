function create(e){
  var sheetName = e.parameters.sheetName;
  try{
    var spread = buildSpread(sheetName);
    var sheet = spread.getSheets()[0];
    buildSheet(sheet);
    try{
      buildUpdateSheet(sheet);
    }catch (error){
      //don't give an error if this bit goes wrong, it's not important
    }
    return 'Sheet created successfully';
  }catch (err){
    return 'Unable to create + format sheet. Error description: ' + err.message;
  }
}

function buildSpread(sheetName){
  //create the spreadsheet
  var spreadsheet = SpreadsheetApp.create(sheetName);
  //save its ID
  saveSpreadsheetId(spreadsheet.getId());
  return spreadsheet;
}

function buildSheet(sheet){
  sheet.setName("Texts");
  //insert headers into the first row
  var range = sheet.getRange(1, 1, 1, 6);
  range.setValues([["Time received", "Their phone number", "Question", "Location", "Toastie flavours", "Original text"]]);
  /*for(var i = 1; i <= 6; i++){
    sheet.autoResizeColumn(i);//Not yet supported by new version of sheets
  }*/
  sheet.getRange(1,1,sheet.getMaxRows(),6).setWrap(true);
  //Save its ID
  saveSheetId(sheet.getSheetId());
}

function buildUpdateSheet(sheet){
  if(getShowUpdate()){
    // show the update message
    var range = sheet.getRange(1, 8, 5, 1);
    var foo = sheet.getRange
    range.setValues([["Tatupload version 2 is now available."],
                     ["It no longer uploads texts using the web browser, but does require up to date Google Play services."],
                     ["You can download it from:"],
                     ["https://play.google.com/store/apps/details?id=uk.org.sucu.tatupload2"],
                     ["(This version will still continue to work if your phone can't handle Google Play services.)"]]);
    // optionally show the stop these option
    var count = getUpdateShownCount();
    if(count > 1){
      range = sheet.getRange(7, 8, 2, 1);
      range.setValues([["Unable to update? Click this link as the account that owns the spreadsheet to stop adding this message to new sheets:"],
                       ["https://script.google.com/macros/s/AKfycbzlHUVyYLlB6oWRCUomhNKBNVfsXD1fPJH61D_RsY8R1uhKmpNz/exec?action=hideUpdate"]]);
    }
    saveUpdateShownCount(count + 1);
  }
}

function testBuild(){
  var e = JSON.parse('{"parameters":{"sheetName":"Test sheet"}}');
  create(e);
  return 'Sheet created successfully';
}