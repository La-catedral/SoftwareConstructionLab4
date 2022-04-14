package debug;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LowestPriceTest {

	
	//test strategy；
	//将优惠分为花钱少 但会买多 以及花钱多两类。
	
	@Test
	public void test() {//Input: [2,5], [[3,0,5],[1,2,10]], [3,2] Output: 14
LowestPrice testOne = new LowestPrice();
		
		List<Integer>  price1 = new ArrayList<>();
		price1.add(2);
		price1.add(3);
		price1.add(4);
		
		List<Integer>  need1 = new ArrayList<>();
		need1.add(1);
		need1.add(2);
		need1.add(1);
		
		List<Integer>  spe11 = new ArrayList<>();
		spe11.add(1);
		spe11.add(1);
		spe11.add(0);
		spe11.add(4);

		List<Integer>  spe12 = new ArrayList<>();
		spe12.add(2);
		spe12.add(2);
		spe12.add(1);
		spe12.add(9);
		
		List<List<Integer>> spec1 = new ArrayList<>();
		spec1.add(spe11);
		spec1.add(spe12);
		
		assertEquals(11,testOne.shoppingOffers(price1, spec1, need1));
		
		List<Integer>  price2 = new ArrayList<>();
		price2.add(2);
		price2.add(5);
		
		List<Integer>  need2 = new ArrayList<>();
		need2.add(3);
		need2.add(2);
		
		List<Integer>  spe21 = new ArrayList<>();
		spe21.add(3);
		spe21.add(0);
		spe21.add(5);

		List<Integer>  spe22 = new ArrayList<>();
		spe22.add(1);
		spe22.add(2);
		spe22.add(10);
		
		List<List<Integer>> spec2 = new ArrayList<>();
		spec2.add(spe21);
		spec2.add(spe22);
		
		assertEquals(14,testOne.shoppingOffers(price2, spec2, need2));
		
		
		
	}

}
