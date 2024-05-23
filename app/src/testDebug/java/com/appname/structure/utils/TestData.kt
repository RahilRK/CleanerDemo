/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appname.structure.utils

import com.base.hilt.model.response.Movie

/**
 * Test data for unit tests.
 */
object TestData {


    val movieItem1 = Movie(
        name = "Abc",
        imageUrl = "", category = "Latest", desc = "Special"
    )
    val movieItem2 = Movie(
        name = "Pqr",
        imageUrl = "", category = "Latest", desc = "Special"
    )

    val movieItem3 = Movie(
        name = "Demo",
        imageUrl = "", category = "Latest", desc = "Special"
    )


    val movieItem4 = Movie(
        name = "Demo2",
        imageUrl = "", category = "Latest", desc = "Special"
    )
    val movieList = listOf(movieItem1, movieItem2, movieItem3, movieItem4)


}
