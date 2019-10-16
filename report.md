1. 
	1. Fulfilled - Not testable since that logic is implemented only in the constructor 
	and only affects private members of the class.
	2. Fulfilled - Testable: assertEq(new Monster().stars, 0);
	3. Fulfilled - Testable: assertEq(new Monster().currentHealth, 10);
	4. Fulfilled - Not testable since the shuffle should be random and thus we cannot
	predict the result.
	5. Not Fulfilled - Not testable (see 4.)
	6. Fulfilled -  Not testable (see 4.)
	7. Fulfilled - Theoretically possible to test but not practically possible since we
	would need to be able to calculate the entire rest of the game in order to make sense
	of how that extra star affected the game.
	8. Fulfilled - Not testable since like in 7. we can't reallistcly test anything inside
	main game loop.
	9. Fulfilled - ??
	10. Fulfilled - ?? 
	11. Fulfilled - ??
	12. Fulfilled - ??
	13. Fulfilled - ?? 
		- Fulfilled - ??
		- Not Fulfilled - ??
	14. Fulfilled - ??
	15. Fulfilled - ??
	16. Fulfilled - ??
	17. Fulfilled - ??
	18. Not Fulfilled - ??
	
