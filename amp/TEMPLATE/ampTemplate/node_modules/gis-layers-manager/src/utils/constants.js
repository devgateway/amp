const RED_GREEN_PALETTE_ID = 9; //id of 
const RED_GREEN_PALETTE_COLORS = 10; // number of colors in red green palette
const BLUE_PURPLE_PALETTE_ID = 10;
const BLUE_PURPLE_PALETTE_COLORS = 5; // number of colors in blue purple palette 
   
var  constants = {
	DEFAULT_LAYERS_PER_PAGE: 10,
	POPULATION_LAYERS_PER_PAGE: 9999999, // set to a big number to fetch all layers	
	AccessType: {
			TEMPORARY: 0,
			PRIVATE: 1,
			PUBLIC: 2,
			STANDARD: 3,
			SHARED: 4
   },
   ASC: 'asc',
   DESC: 'desc',
   NUMBER_OF_COLORS: {}
};

constants.NUMBER_OF_COLORS[RED_GREEN_PALETTE_ID] = RED_GREEN_PALETTE_COLORS;
constants.NUMBER_OF_COLORS[BLUE_PURPLE_PALETTE_ID] = BLUE_PURPLE_PALETTE_COLORS;

module.exports = constants;