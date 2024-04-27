#include <iostream>

int main() {
    int num;
    int result = 0;
    
    for (int i = 0; i < 21; ++i) {
        std::cin >> num;
        result ^= num;
    }

    std::cout << result << std::endl;
}