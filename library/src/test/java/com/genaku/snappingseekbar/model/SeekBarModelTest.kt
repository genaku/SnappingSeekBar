package com.genaku.snappingseekbar.model

import io.kotlintest.IsolationMode
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import kotlin.math.roundToInt

class SeekBarModelTest : FreeSpec() {

    override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerLeaf

    private val model = SeekBarModel()

    init {
        val items = generateItems(10)
        model.setItems(items)

        "check round" {
            1.5.roundToInt() shouldBe 2
            1.3.roundToInt() shouldBe 1
        }

        "first" {
            model.getItem(2) shouldBe items[2]
            model.getTitle(3) shouldBe "3"
        }

        "initial range" - {
            model.getIdx(20f) shouldBe 0

            "range set to 100" - {
                model.setWidth(100f)
                model.getIdx(20f) shouldBe 2

                "find nearest" {
                    model.getIdx(18f) shouldBe 2
                    model.getIdx(21f) shouldBe 2
                    model.getIdx(25f) shouldBe 3
                }

                "set 100 items" {
                    val items2 = generateItems(100)
                    model.setItems(items2)
                    model.getIdx(20f) shouldBe 20
                    model.getTitle(20) shouldBe "20"
                }
            }
        }
    }

    private fun generateItems(size: Int): List<ISeekBarItem> {
        val items = mutableListOf<SimpleSeekBarItem>()
        for (i in 0 until size) {
            items.add(SimpleSeekBarItem("$i"))
        }
        return items
    }
}