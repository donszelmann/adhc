package ch.cern.atlas.adhc.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.cern.atlas.adhc.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Adhc implements EntryPoint {
	@UiField
	VerticalPanel details;

	@UiTemplate("Adhc.ui.xml")
	interface Binder extends UiBinder<VerticalPanel, Adhc> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private List<Entry> shiftTable[];
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		shiftTable = new List[8];
		shiftTable[0] = new ArrayList<Entry>();
		shiftTable[0].add(new Entry());

		shiftTable[1] = new ArrayList<Entry>();
		shiftTable[1].add(new Entry());
		shiftTable[1].add(new Entry());
		shiftTable[1].add(new Entry());

		shiftTable[2] = new ArrayList<Entry>();
		shiftTable[2].add(new Entry());
		shiftTable[2].add(new Entry());
		shiftTable[2].add(new Entry());
		shiftTable[2].add(new Entry());
		shiftTable[2].add(new Entry());
		shiftTable[2].add(new Entry());
		shiftTable[2].add(new Entry());
		shiftTable[2].add(new Entry());

		shiftTable[3] = new ArrayList<Entry>();
		shiftTable[3].add(new Entry());
		shiftTable[3].add(new Entry());
		shiftTable[3].add(new Entry());

		shiftTable[4] = new ArrayList<Entry>();
		shiftTable[4].add(new Entry());
		shiftTable[4].add(new Entry());
		shiftTable[4].add(new Entry());

		shiftTable[5] = new ArrayList<Entry>();
		shiftTable[5].add(new Entry());
		shiftTable[5].add(new Entry());

		shiftTable[6] = new ArrayList<Entry>();
		shiftTable[6].add(new Entry());
		shiftTable[6].add(new Entry());
		shiftTable[6].add(new Entry());

		shiftTable[7] = new ArrayList<Entry>();
		shiftTable[7].add(new Entry());
		shiftTable[7].add(new Entry());

		
		VerticalPanel details = binder.createAndBindUi(this);

		// Navigation
		StackLayoutPanel stackLayoutPanel = new StackLayoutPanel(Unit.EM);
		stackLayoutPanel.setStyleName("gwt-StackLayoutPanel");
		// stackLayoutPanel.setWidth("230");
		double headerSize = 2.3;
		stackLayoutPanel.add(createShiftsPanel(), createHeader("Shifts", null),
				headerSize);
		stackLayoutPanel.add(new HTML("that"), createHeader("That", null),
				headerSize);
		stackLayoutPanel.add(new HTML("the other"), createHeader("The Other",
				null), headerSize);

		// Create a three-pane layout with splitters.
		SplitLayoutPanel splitlayoutPanel = new SplitLayoutPanel();
		splitlayoutPanel.addWest(stackLayoutPanel, 250);
		// splitlayoutPanel.addWest(new Button("list"), 128);
		// splitlayoutPanel.addNorth(new Button("Header"), 384);
		// splitlayoutPanel.addSouth(new Button("Footer"), 384);
		// splitlayoutPanel.addEast(new Button("east"), 128);
		splitlayoutPanel.add(createShiftTable());

		// Attach the LayoutPanel to the RootLayoutPanel. The latter will listen
		// for
		// resize events on the window to ensure that its children are informed
		// of
		// possible size changes.
		RootLayoutPanel rootPanel = RootLayoutPanel.get();
		rootPanel.add(splitlayoutPanel);

		// LeftOver, REMOVE
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		// RootPanel.get("nameFieldContainer").add(nameField);
		// RootPanel.get("sendButtonContainer").add(sendButton);
		// RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}

	private Widget createShiftTable() {
		FlexTable table = new FlexTable();
		table.setStylePrimaryName("controlroom");
		
		table.setWidget(0, 1, createTaskTable(shiftTable[0]));
		table.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		
		table.setWidget(1, 0, createTaskTable(shiftTable[1]));
		table.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

		table.setWidget(1, 1, createTaskTable(shiftTable[2]));
		table.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);

		table.setWidget(1, 2, createTaskTable(shiftTable[3]));
		table.getCellFormatter().setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_TOP);

		table.setWidget(2, 0, createTaskTable(shiftTable[4]));
		table.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);

		table.setWidget(2, 1, createTaskTable(shiftTable[5]));
		table.getCellFormatter().setVerticalAlignment(2, 1, HasVerticalAlignment.ALIGN_TOP);

		table.setWidget(2, 2, createTaskTable(shiftTable[6]));
		table.getCellFormatter().setVerticalAlignment(2, 2, HasVerticalAlignment.ALIGN_TOP);

		table.setWidget(3, 1, createTaskTable(shiftTable[7]));
		table.getCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_TOP);

		return table;
	}

	private Widget createTaskTable(List<Entry> entries) {
		FlexTable table = new FlexTable();
		// FIXME
		table.setBorderWidth(1);
		table.setStylePrimaryName("tasks");
		int row = 0;
		for (Iterator<Entry> i = entries.iterator(); i.hasNext(); ) {
			Entry e = i.next();
			
			Widget task = new Hyperlink(e.getTaskName(), "TBD");
			task.setStylePrimaryName("taskName");
			table.setWidget(row, 0, task);
			
			Widget picture = e.getPicture();
			picture.setStylePrimaryName("picture");
			table.setWidget(row, 1, picture);
			
			Widget shifter = new Hyperlink(e.getName(), "TBD");
			shifter.setStylePrimaryName("shifter");
			table.setWidget(row, 2, shifter);
			
			Widget period = new Label(e.getPeriod());
			period.setStylePrimaryName("period");
			table.setWidget(row, 3, period);	
			
			row++;
		}
		return table;
	}
	
	/**
	 * Create the Shifts Panel.
	 * 
	 * @return the list of shifts
	 */
	private VerticalPanel createShiftsPanel() {
		// Create the list of shifts and calendar
		VerticalPanel type = new VerticalPanel();
		String group = "shiftGroup";
		type.add(new RadioButton(group, "Online"));
		type.add(new RadioButton(group, "Offline"));
		type.add(new RadioButton(group, "On Call"));

		VerticalPanel shiftsPanel = new VerticalPanel();
		shiftsPanel.setSpacing(4);
		shiftsPanel.add(type);

		shiftsPanel.add(new DatePicker());
		return shiftsPanel;
	}

	/**
	 * Get Widget of the header that includes an image and some text.
	 * 
	 * @param text
	 *            the header text
	 * @param image
	 *            the {@link ImageResource} to add next to the header
	 * @return the header as a string
	 */
	private Widget createHeader(String text, ImageResource image) {
		// Add the image and text to a horizontal panel
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(0);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		if (image != null)
			panel.add(new Image(image));
		Button headerText = new Button(text);
		// FIXME
		headerText.setStyleName("cw-StackPanelHeader");
		panel.add(headerText);

		return panel;
	}

}