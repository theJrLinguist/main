package seedu.address.logic.commands.eventcommands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.Poll;

import static java.util.Objects.requireNonNull;

/**
 * Command to display a poll of a pre-selected event given an index.
 */
public class DisplayPollCommand extends Command {

    public static final String COMMAND_WORD = "displayPoll";

    public static final String MESSAGE_USAGE = COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Poll %1$s displayed.";

    private final Index targetIndex;
    private Event event;

    /**
     * Creates an DisplayPollCommand to add the specified {@code Event}
     */
    public DisplayPollCommand(Index index) {
        requireNonNull(index);
        this.targetIndex = index;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        event = history.getSelectedEvent();
        try {
            Poll poll = event.getPoll(targetIndex);
            String result = String.format(MESSAGE_SUCCESS, targetIndex.getOneBased());
            result += '\n' + poll.displayPoll();
            return new CommandResult(result);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException("No poll exists at this index.");
        }
    }
}
