#include <iostream>
#include <string>
#include "BUtils.cpp"
#include "BInteger.cpp"
#include "BBoolean.cpp"
#include "sort_m2_data1000.cpp"

#ifndef sort_m2_data1000_exec_H
#define sort_m2_data1000_exec_H

using namespace std;

class sort_m2_data1000_exec {

    public:

    private:


        sort_m2_data1000 _sort_m2_data1000;


        BInteger counter;
        BInteger sorted;

    public:

        sort_m2_data1000_exec() {
            counter = (BInteger(0));
            sorted = (BInteger(0));
        }

        void simulate() {
            while((sorted.less((BInteger(1000)))).booleanValue()) {
                counter = (BInteger(0));
                while((counter.less((BInteger(1999)).minus((BInteger(2)).multiply(sorted)))).booleanValue()) {
                    this->_sort_m2_data1000.prog2();
                    counter = counter.plus((BInteger(1)));
                }
                counter = (BInteger(0));
                while((counter.less(sorted)).booleanValue()) {
                    this->_sort_m2_data1000.prog1();
                    counter = counter.plus((BInteger(1)));
                }
                this->_sort_m2_data1000.progress();
                sorted = sorted.plus((BInteger(1)));
            }
            while((sorted.less((BInteger(1999)))).booleanValue()) {
                counter = (BInteger(0));
                while((counter.less((BInteger(1999)).minus(sorted))).booleanValue()) {
                    this->_sort_m2_data1000.prog1();
                    counter = counter.plus((BInteger(1)));
                }
                this->_sort_m2_data1000.progress();
                sorted = sorted.plus((BInteger(1)));
            }
            this->_sort_m2_data1000.final_evt();
        }

};

int main() {
    sort_m2_data1000_exec exec;
    exec.simulate();
    return 0;
}

#endif