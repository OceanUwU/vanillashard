package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import javassist.CtBehavior;
import java.util.Map;



@SpirePatch(
    clz=CardLibrary.class,
    method="getAnyColorCard",
    paramtypez={
        AbstractCard.CardType.class,
        AbstractCard.CardRarity.class
    }
)
public class RestrictCardColorWithType {
    public static AbstractCard.CardColor vanillaColors[] = { AbstractCard.CardColor.RED, AbstractCard.CardColor.GREEN, AbstractCard.CardColor.BLUE, AbstractCard.CardColor.PURPLE, AbstractCard.CardColor.COLORLESS, AbstractCard.CardColor.CURSE };
        
    @SpireInsertPatch(
        rloc=11,
        localvars={"anyCard"}
    )
    public static void Insert(AbstractCard.CardType type, AbstractCard.CardRarity rarity, CardGroup anyCard) {
        CardGroup cardsToRemove = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (AbstractCard card : anyCard.group) {
            boolean available = false;

            if (card.color == AbstractDungeon.player.getCardColor()) {
                available = true;
            } else {
                for (AbstractCard.CardColor color : vanillaColors) {
                    if (card.color == color) {
                        available = true;
                        break;
                    }
                }
            }

            if (!available) {
                cardsToRemove.addToBottom(card); 
            }
        }

        for (AbstractCard card : cardsToRemove.group) {
            anyCard.removeCard(card);
        }
    }
}