function saveSpreadsheetId(id){
  var properties = PropertiesService.getUserProperties();
  properties.setProperty(ssKey(), id);
}

function saveSheetId(id){
  var properties = PropertiesService.getUserProperties();
  properties.setProperty(sheetKey(), id);
}

function saveUpdateShownCount(count){
  var properties = PropertiesService.getUserProperties();
  properties.setProperty(updateCountKey(), count);
}

function saveShowUpdate(show){
  if (show){
    saveUpdateShownCount(0);
  } else {
    saveUpdateShownCount(-1);
  }
}

function blockUpdateTest(){
  saveShowUpdate(false);
}

function allowUpdateTest(){
  saveShowUpdate(true);
}

function getSpreadsheetId(){
  var properties = PropertiesService.getUserProperties();
  var id = properties.getProperty(ssKey());
  return id;
}

function getSheetId(){
  var properties = PropertiesService.getUserProperties();
  var id = properties.getProperty(sheetKey());
  return id;
}

function getUpdateShownCount(){
  var properties = PropertiesService.getUserProperties();
  var count = properties.getProperty(updateCountKey());
  if (count == null) {
    return 0;
  } else {
    return parseInt(count, 10); // Convert string to base 10 integer
  }
}

function getShowUpdate(){
  var count = getUpdateShownCount();
  if (count == -1){
    return false;
  } else {
    return true;
  }
}

function ssKey(){
  return "spreadsheet_ID";
}

function sheetKey(){
  return "sheet_ID";
}

function updateCountKey(){
  return "update_shown_count";
}