//@@author theJrLinguist
package seedu.address.logic.commands.eventcommands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalEvents.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.NoEventSelectedException;
import seedu.address.logic.commands.exceptions.NoUserLoggedInException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.event.Event;
import seedu.address.model.event.exceptions.NotEventOrganiserException;
import seedu.address.model.person.Person;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.PersonBuilder;

public class AddPollCommandTest {
    private static final String POLLNAME = EventBuilder.DEFAULT_POLL;

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_acceptedAddPoll() throws NoEventSelectedException, NoUserLoggedInException,
            NotEventOrganiserException {
        AddPollCommand command = new AddPollCommand(POLLNAME);
        Person user = ALICE;
        model.setCurrentUser(user);
        Event event = model.getFilteredEventList().get(0);
        model.setSelectedEvent(event);
        String expectedMessage = String.format(command.MESSAGE_SUCCESS, POLLNAME, event);
        expectedModel.setSelectedEvent(event);
        expectedModel.setCurrentUser(user);
        expectedModel.addPoll(POLLNAME);
        expectedModel.updateEvent(event, event);
        expectedModel.commitAddressBook();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noUserAddPoll() {
        AddPollCommand command = new AddPollCommand(POLLNAME);
        Event event = new EventBuilder().build();
        model.setSelectedEvent(event);
        String expectedMessage = String.format(Messages.MESSAGE_NO_USER_LOGGED_IN);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_noEventAddPoll() {
        Person user = new PersonBuilder().build();
        model.setCurrentUser(user);
        AddPollCommand command = new AddPollCommand(POLLNAME);
        String expectedMessage = String.format(Messages.MESSAGE_NO_EVENT_SELECTED);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_notEventOrganiserAddPoll() {
        AddPollCommand command = new AddPollCommand(POLLNAME);
        Person user = new PersonBuilder().build();
        model.setCurrentUser(user);
        Person anotherUser = new PersonBuilder(user).withName("Bob").build();
        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.withOrganiser(anotherUser);
        Event event = eventBuilder.build();
        model.setSelectedEvent(event);
        String expectedMessage = String.format(Messages.MESSAGE_NOT_EVENT_ORGANISER);
        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }
}
