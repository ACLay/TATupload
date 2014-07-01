//makes a form, a spreadsheet to store it's responses, and returns the forms' ID
function createForm(e) {
  var formName = e.parameters.formName;
  //make the form
  var title = 'Text A Toastie';
  var description = 'What do they want?';

  var form = FormApp.create(formName)
      .setDescription(description)
      .setConfirmationMessage('Toastie request uploaded!');

  form.addTextItem()
      .setTitle('What is their number?')
      .setRequired(true);

  form.addTextItem()
      .setTitle('What do they want to know?')
      .setRequired(true);
  
  form.addTextItem()
      .setTitle('Where are they?')
      .setRequired(true);

  form.addTextItem()
      .setTitle('What toastie do they want?')
      .setRequired(true);
  
  form.addTextItem()
      .setTitle('Original message')
      .setRequired(false);
    
  //form responses seem to be timestamped on submission, so that's not delt with here :)
  
  //make the spreadsheet to send responses to
  var sheet = SpreadsheetApp.create(formName + ' (responses)');
  var spreadsheetID = sheet.getId() /*I don't have a semicolon here, why does it work?!?*/
  //TODO implement timezone in app settings
  sheet.setSpreadsheetTimeZone('Europe/London');
  form.setDestination(FormApp.DestinationType.SPREADSHEET, spreadsheetID);
  
  return 'Form created';
}