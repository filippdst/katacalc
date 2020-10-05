package com.nonstech.katacalc;

import com.nonstech.katacalc.exceptions.BadInputException;
import com.nonstech.katacalc.exceptions.NegativeNumberException;
import com.nonstech.katacalc.impl.CalculatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KatacalcApplicationTests {

	private Calculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new CalculatorImpl();
	}

	@Test
	public void shouldReturnZeroWhenGotEmptyString() {
		int result = calculator.add("");
		assertThat(result).isEqualTo(0);
	}

	@Test
	public void shouldReturnNumberWhenGivenNumber() {
		int result = calculator.add("1");
		assertThat(result).isEqualTo(1);
	}

	@Test
	public void shouldAddTwoNumbersSeparatedByComma(){
		int result = calculator.add("1,2");
		assertThat(result).isEqualTo(3);
	}

	@Test
	public void shouldAddThreeNumbersSeparatedByComma(){
		int result = calculator.add("1,2,3");
		assertThat(result).isEqualTo(6);
	}

	@Test
	public void shouldAddThreeNumbersSeparatedByCommaAndNewLine(){
		int result = calculator.add("1,2\n4");
		assertThat(result).isEqualTo(7);
	}

	@Test
	public void shouldThrowExceptionWhenInputHasBadEnd(){
		Assertions.assertThrows(BadInputException.class, () -> {
			calculator.add("1,2,/n");
		});
	}

	@Test
	public void shouldThrowExceptionWhenNotNumberProvided(){
		Assertions.assertThrows(BadInputException.class, () -> {
			calculator.add("1,2,ccc");
		});
	}

	@Test
	public void shouldThrowExceptionWhenNegativeNumberProvided(){
		Assertions.assertThrows(NegativeNumberException.class, () -> {
			calculator.add("-1");
		});
	}

	@Test
	public void shouldIncludeNegativeNumbersInExceptionWhenNegativeNumberProvided(){
		NegativeNumberException exception = Assertions.assertThrows(NegativeNumberException.class, () -> {
			calculator.add("-1,-2,-3");
		});
		assertThat(exception.getMessage()).isEqualTo("Negatives not allowed! Negatives: -1 -2 -3");
	}

	@Test
	public void shouldAddTwoNumbersSeparatedByCommaWithNonStandardDelimiter(){
		int result = calculator.add("//;\n1;2");
		assertThat(result).isEqualTo(3);
	}

	@Test
	public void shouldAddThreeNumbersAndSkipGreaterThan1000(){
		int result = calculator.add("1,2,3,1001");
		assertThat(result).isEqualTo(6);
	}

	@Test
	public void shouldAddTwoNumbersSeparatedByCommaWithNonStandardDelimiterInPlaceholder(){
		int result = calculator.add("//[;][ooo]\n1;2ooo3");
		assertThat(result).isEqualTo(6);
	}

}
