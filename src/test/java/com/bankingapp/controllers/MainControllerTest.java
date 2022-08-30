package com.bankingapp.controllers;

import com.bankingapp.utils.TestUtility;
import com.bankingapp.utils.constants.Constants;
import com.bankingapp.utils.di.BindingModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Order(1)
public class MainControllerTest {
    private MainController mainController;

    MainControllerTest() {
        Injector injector = Guice.createInjector(new BindingModule());
        this.mainController = injector.getInstance(MainController.class);
    }


    @ParameterizedTest
    @MethodSource("getValidTestFiles")
    void verifyPerform_ValidInput_ShouldMatchTheExpectedOutputInFile(String inputFilePath,
                                                                     String outputFilePath) throws Exception {
        //Given
        File inputFile = new File(getClass().getClassLoader().getResource(inputFilePath).toURI());
        File outputFile = new File(getClass().getClassLoader().getResource(outputFilePath).toURI());
        List<String> expectedOutput = TestUtility.getFileAsList(outputFile);

        //When
        List<String> actualOutput = mainController.perform(inputFile);
        displayOutput(actualOutput, "Actual Output");

        //Then
        assertEquals(expectedOutput, actualOutput);
    }

    @ParameterizedTest
    @MethodSource("getInvalidTestFiles")
    void verifyPerform_ValidInput_ShouldMatchTheExpectedOutputInFile(String inputFilePath) throws Exception {
        //Given
        File inputFile = new File(getClass().getClassLoader().getResource(inputFilePath).toURI());

        //When
        List<String> actualOutput = mainController.perform(inputFile);
        displayOutput(actualOutput, "Actual Output");

        //Then
        assertNotNull(actualOutput);
        assertFalse(actualOutput.isEmpty());
        assertTrue(actualOutput.get(0).equals(Constants.Messages.COMMAND_INVALID));

    }

    private static Stream<Arguments> getValidTestFiles() {
        return Stream.of(
                Arguments.of("SampleInputFiles/Sample1.txt",
                        "SampleOutputFiles/Output1.txt")
        );
    }

    private static Stream<Arguments> getInvalidTestFiles() {
        return Stream.of(
                Arguments.of("SampleInputFiles/SampleErr1.txt")
        );
    }

    private void displayOutput(List<String> output, String message) {
        System.out.println("****" + message + "****");
        output.forEach(System.out::println);
    }
}
