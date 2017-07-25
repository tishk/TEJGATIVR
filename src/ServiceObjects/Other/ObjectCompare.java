package ServiceObjects.Other;

import ServiceObjects.Pan.BalanceForCard;

/**
 * Created by Administrator on 12/23/2015.
 */
public class ObjectCompare {
    public boolean isBalanceForCard(Object obj){
        BalanceForCard balanceForCard=new BalanceForCard();
        return balanceForCard.getClass().isInstance(obj);
    }
}
