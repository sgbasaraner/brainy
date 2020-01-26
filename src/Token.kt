// > 	Move the pointer to the right
// < 	Move the pointer to the left
// + 	Increment the memory cell under the pointer
// - 	Decrement the memory cell under the pointer
// [ 	Jump past the matching ] if the cell under the pointer is 0
// ] 	Jump back to the matching [ if the cell under the pointer is nonzero
// . 	Output the character signified by the cell at the pointer
// , 	Input a character and store it in the cell at the pointer
enum class Token(val char: Char) {
    INCREMENT_PTR('>'),
    DECREMENT_PTR('<'),
    INCREMENT_VAL('+'),
    DECREMENT_VAL('-'),
    JUMP_FORWARD('['),
    JUMP_BACK(']'),
    PRINT_CHAR('.'),
    GET_CHAR(',')
}