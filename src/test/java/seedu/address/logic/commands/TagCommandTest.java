package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.TagCommand.MESSAGE_ADD_TAG_SUCCESS;
import static seedu.address.logic.commands.TagCommand.MESSAGE_DUPLICATE;
import static seedu.address.testutil.TypicalPersons.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Id;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class TagCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        Id firstStudentId = new Id("A1029384A");
        HashSet<Tag> firstTagList = new HashSet<Tag>();
        firstTagList.add(new Tag("TopStudent"));
        firstTagList.add(new Tag("PotentialTA"));

        Id secondStudentId = new Id("A1029384A");
        HashSet<Tag> secondTagList = new HashSet<Tag>();
        secondTagList.add(new Tag("TopStudent"));
        secondTagList.add(new Tag("PotentialTA"));

        Id thirdStudentId = new Id("A2129334F");
        HashSet<Tag> thirdTagList = new HashSet<Tag>();
        thirdTagList.add(new Tag("BadStudent"));
        thirdTagList.add(new Tag("GoodStudent"));

        TagCommand firstTagCommand = new TagCommand(firstStudentId, firstTagList);
        TagCommand secondTagCommand = new TagCommand(secondStudentId, secondTagList);
        TagCommand thirdTagCommand = new TagCommand(thirdStudentId, thirdTagList);

        // same object -> returns true
        assertTrue(firstTagCommand.equals(firstTagCommand));

        // same values -> returns true
        assertTrue(firstTagCommand.equals(secondTagCommand));

        // different types -> returns false
        assertFalse(firstTagCommand.equals(1));

        // null -> returns false
        assertFalse(firstTagCommand.equals(null));

        // different person -> returns false
        assertFalse(firstTagCommand.equals(thirdTagCommand));
    }

    @Test
    public void execute_noStudentFound_throwCommandException() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("TopStudent"));
        Id invalidId = new Id("A0912124E");
        TagCommand tagCommand = new TagCommand(invalidId, tagList);

        assertCommandFailure(tagCommand, model, TagCommand.MESSAGE_PERSON_NOTFOUND);
    }

    //workingon, make good use of the util provided instead of adding by your own.
    @Test
    public void execute_totallyDuplicateTag_success() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("friends"));
        Id invalidId = new Id("A0265901E");
        TagCommand tagCommand = new TagCommand(invalidId, tagList);
        String expectedMessage = String.format(MESSAGE_ADD_TAG_SUCCESS, tagList) + " \n"+ MESSAGE_DUPLICATE;

        assertCommandSuccess(tagCommand, model, expectedMessage, model);
    }
    @Test
    public void execute_partiallyDuplicateTag_success() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("friends"));
        tagList.add(new Tag("topstudent"));
        Id invalidId = new Id("A0265901E");
        TagCommand tagCommand = new TagCommand(invalidId, tagList);
        String expectedMessage = String.format(MESSAGE_ADD_TAG_SUCCESS, tagList) + " \n"+ MESSAGE_DUPLICATE;

        assertCommandSuccess(tagCommand, model, expectedMessage, model);
    }

    @Test
    public void execute_validId_validTag_success() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("TopStudent"));
        Id invalidId = new Id("A0251893P");
        TagCommand tagCommand = new TagCommand(invalidId, tagList);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person resultPerson =  new PersonBuilder().withName("Alice Pauline").withId("A0251893P")
                .withMajor("Computer Science").withIntake("2023")
                .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
                .withPhone("94351253")
                .withTags("friends","TopStudent").build();
        expectedModel.setPerson(ALICE, resultPerson);
        assertCommandSuccess(tagCommand, model, String.format(MESSAGE_ADD_TAG_SUCCESS, tagList), expectedModel);
    }
    @Test
    public void toStringMethod() {
        HashSet<Tag> tagList = new HashSet<Tag>();
        tagList.add(new Tag("TopStudent"));
        Id invalidId = new Id("A0912124E");
        TagCommand tagCommand = new TagCommand(invalidId, tagList);

        String expected = TagCommand.class.getCanonicalName() + "{Id="+"A0912124E, "+"Tags="+tagList+"}";
        assertEquals(expected, tagCommand.toString());
    }
}