package com.amirhusseinsoori.adavancetesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.amirhusseinsoori.adavancetesting.getOrAwaitValue
import com.amirhusseinsoori.adavancetesting.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import com.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

//@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {



    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // You can use this rule for your host side tests that use Architecture Components.
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


//    private lateinit var dataBase: ShoppingDataBase
    @Inject
    @Named("test_db")
    lateinit var dataBase: ShoppingDataBase
    private lateinit var dao: ShoppingDao


    @Before
    fun setup() {
        hiltRule.inject()
 /*       //because its not real dataBase
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingDataBase::class.java
        ).allowMainThreadQueries().build()*/
        dao = dataBase.shoppingDao()
    }

    @After
    fun teardown() {
        dataBase.close()
    }

    // This method requires that all coroutines launched inside [testBody] complete, or are cancelled, as part of the test  conditions.

    @Test
    fun testLaunchFragmentInHiltContainer() {
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }
    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("ali", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem).contains(shoppingItem)

    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("ali", 1, 1f, "url", id = 1)
        //reason use the insertFun again because dataBase is Empty
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItem).doesNotContain(shoppingItem)

    }

    @Test
    fun observeTotalPriceTime() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("ali", 5, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("ali", 10, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("ali", 0, 1f, "url", id =3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)


        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPriceSum).isEqualTo(5 * 10f + 10 * 5.5f)
    }
}