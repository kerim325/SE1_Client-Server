package gameData.map.halfGameMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class HalfGameMapGeneratorTests {

	@Test
	public void givenHalfGameMapChecker_generateHalfGameMap_expectUsageOfHalfGameMapChecker() {
		// arrange
		HalfGameMapChecker mockChecker = Mockito.mock(HalfGameMapChecker.class);
		HalfGameMapGenerator halfGameMapGenerator = new HalfGameMapGenerator(mockChecker);
		when(mockChecker.checkHalfGameMap(Mockito.any(HalfGameMap.class))).thenReturn(false).thenReturn(false)
				.thenReturn(true);

		// act
		HalfGameMap halfGameMap = halfGameMapGenerator.generateHalfGameMap();

		// assert
		ArgumentCaptor<HalfGameMap> argumentCaptor = ArgumentCaptor.forClass(HalfGameMap.class);
		Mockito.verify(mockChecker, Mockito.times(3)).checkHalfGameMap(argumentCaptor.capture());

		List<HalfGameMap> capturedArguments = argumentCaptor.getAllValues();
		assertThat(capturedArguments.get(2), is(equalTo(halfGameMap)));
	}

	@Test
	public void givenHalfGameMapGenerator_generateHalfGameMap_expectHalfGameMapWithCorrectWidthAndHeight() {
		// arrange
		HalfGameMapGenerator halfGameMapGenerator = new HalfGameMapGenerator();

		// act
		HalfGameMap halfGameMap = halfGameMapGenerator.generateHalfGameMap();

		// assert
		assertThat(halfGameMap.getMaxValX(), is(equalTo(HalfGameMapGenerator.getxMax())));
		assertThat(halfGameMap.getMaxValY(), is(equalTo(HalfGameMapGenerator.getyMax())));
	}

}
