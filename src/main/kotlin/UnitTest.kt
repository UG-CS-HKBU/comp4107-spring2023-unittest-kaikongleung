import junit.framework.TestCase.assertTrue
import org.junit.Test

class UnitTest {
    @Test
    fun testCaoDodgeAttack() {
        monarchHero = CaoCao()
        for (i in 1..7){
            heroes.add(NoneMonarchFactory.createRandomHero())
        }
        assertTrue(monarchHero.dodgeAttack())
    }

    @Test
    fun testBeingAttacked() {
        if (heroes.size==0)
            heroes.add(NoneMonarchFactory.createRandomHero())
        for (i in heroes) {
            val hero = i
            val spy = object: Hero(hero.role) {
                override val name = hero.name
                override fun beingAttacked() {
                    hero.beingAttacked()
                    assertTrue(hero.hp >= 0)
                }
            }
            for(j in 1..10)
                spy.beingAttacked()
        }
    }

    @Test
    fun testDiscardCards() {
        val dummy = DummyRole()
        val hero = ZhangFei(dummy)
        hero.discardCards()
        assertTrue(hero.hp>=hero.numOfCards)
    }
}

object FakeNonmonarchFactory: GameObjectFactory {
    var count = 0
    var last: WeiHero? = null
    init {
        monarchHero = CaoCao()
    }
    override fun getRandomRole(): Role =
        MinisterRole()
    override fun createRandomHero(): Hero {
        val hero = when(count++) {
            0->SimaYi(getRandomRole())
            1->XuChu(getRandomRole())
            else->XiaHouyuan(getRandomRole())
        }
        val cao = monarchHero as CaoCao
        if (last == null)
            cao.helper = hero
        else
            last!!.setNext(hero)
        last = hero
        return hero
    }
}

object FakeMonarchFactory: GameObjectFactory {
    var count = 0
    override fun getRandomRole(): Role =
        MonarchRole()
    override fun createRandomHero(): Hero {
        val hero = when(count++) {
            0->CaoCao()
            else -> {CaoCao()}
        }
        return hero
    }
}

class CaoCaoUnitTest{
    @Test
    fun testCaoDodgeAttack() {
        monarchHero = FakeMonarchFactory.createRandomHero() as MonarchHero
        heroes.add(monarchHero)
        monarchHero.setCommand(Abandon(monarchHero))
        for (i in 0..2) {
            var hero = FakeNonmonarchFactory.createRandomHero()
            hero.index = heroes.size;
            heroes.add(hero)
        }
        for (hero in heroes) {
            hero.beingAttacked()
            hero.templateMethod()
        }
        assertTrue(monarchHero.dodgeAttack())
    }
}

class DummyRole :Role{
    override val roleTitle = "Dummy"
    override fun getEnemy() = ""
}