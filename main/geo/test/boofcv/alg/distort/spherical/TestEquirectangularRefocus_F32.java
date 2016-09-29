/*
 * Copyright (c) 2011-2016, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.distort.spherical;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestEquirectangularRefocus_F32 {

	/**
	 * Sees if recentering moves it to approximately the expected location
	 */
	@Test
	public void simpleTests() {
		Point2D_F32 found = new Point2D_F32();

		EquirectangularRefocus_F32 alg = new EquirectangularRefocus_F32();

		// this is the standard configuration and there should be no change
		alg.configure(300,250,0,0);
		alg.compute(299.0f*0.5f, 249*0.5f, found);
		assertEquals( 0 , found.distance(299.0f*0.5f, 249*0.5f), GrlConstants.FLOAT_TEST_TOL);

		alg.configure(300,250, (float)Math.PI/2.0f,0);
		alg.compute(299.0f*0.5f, 249*0.5f, found);
		assertEquals( 0 , found.distance(299.0f*0.75f, 249*0.5f), GrlConstants.FLOAT_TEST_TOL);

		alg.configure(300,250, 0, (float)Math.PI/4.0f);
		alg.compute(299.0f*0.5f, 249*0.5f, found);
		assertEquals( 0 , found.distance(299.0f*0.5f, 249*0.75f), GrlConstants.FLOAT_TEST_TOL);

	}
}