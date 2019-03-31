#include <iostream>
#include <string>
#include <cstdarg>
#include <immer/set.hpp>
#include "BCouple.cpp"
#include "BSet.cpp"

#ifndef BRELATION_H
#define BRELATION_H


using namespace std;

template<typename S, typename T>
class BRelation : public BSet<BCouple<S,T>> {

    public:

        struct Hash {
            public:
                size_t operator()(const BCouple<S,T>& obj) const {
                    return obj.hashCode();
                }
        };

        struct HashEqual {
            public:
                bool operator()(const BCouple<S,T>& obj1, const BCouple<S,T>& obj2) const {

                    if (obj1 == obj2)
                        return true;
                    else
                        return false;
                }
        };

        BRelation<S,T>(const immer::set<BCouple<S,T>, Hash, HashEqual>& elements) {
            this->set = elements;
        }

        template<typename... Args>
        BRelation<S,T>(const Args&... args) {
          this->set = var(args...);
        }

        immer::set<BCouple<S,T>, BRelation<S,T>::Hash, BRelation<S,T>::HashEqual> var() {
          return immer::set<BCouple<S,T>, BRelation<S,T>::Hash, BRelation<S,T>::HashEqual>();
        }

        template<typename R, typename... Args>
        immer::set<BCouple<S,T>, BRelation<S,T>::Hash, BRelation<S,T>::HashEqual> var(const R& first, const Args&... args) {
          return var(args...).insert(first);
        }

        BRelation<S,T>() {
            this->set = immer::set<BCouple<S,T>, typename BRelation<S,T>::Hash, typename BRelation<S,T>::HashEqual>();
        }

        BRelation<S,T>(const BRelation<S,T>& set) {
            this->set = set.set;
        }

        BInteger card() {
            return BInteger(set.size());
        }

        BRelation<S,T> _union(const BRelation<S,T>& set) const {
            if(this->size() > set.size()) {
                immer::set<BCouple<S,T>,Hash, HashEqual> result = this->set;
                for (const BCouple<S,T>& obj : set.set) {
                    if(result.count(obj) == 0) {
                        result = std::move(result).insert(obj);
                    }
                }
                return BRelation(result);
            } else {
                immer::set<BCouple<S,T>,Hash, HashEqual> result = set.set;
                for (const BCouple<S,T>& obj : this->set) {
                    if(result.count(obj) == 0) {
                        result = std::move(result).insert(obj);
                    }
                }
                return BRelation(result);
            }
        }

        BSet<T> relationImage(const BSet<S>& domain) {
            immer::set<T,typename BSet<T>::Hash, typename BSet<T>::HashEqual> result;
            for(BCouple<S,T> couple : this->set) {
                if(!(domain.contains(couple.projection1()))) {
                    result = result.insert(couple.projection2());
                }
            }
            return BSet<T>(result);
        }


        T functionCall(const S& arg) {
            for(BCouple<S,T> couple : this->set) {
                if(couple.projection1() == arg) {
                    return couple.projection2();
                }
            }
            throw runtime_error("Argument is not in the key set of this map");
        }

    	BSet<S> domain() const {
    	    immer::set<S,typename BSet<S>::Hash, typename BSet<S>::HashEqual> result;
    	    for(BCouple<S,T> couple : this->set) {
                result = result.insert(couple.projection1());
    	    }
            return BSet<S>(result);
    	}

    	BSet<T> range() const {
    	    immer::set<T,typename BSet<T>::Hash, typename BSet<T>::HashEqual> result;
    	    for(BCouple<S,T> couple : this->set) {
                result = result.insert(couple.projection2());
    	    }
            return BSet<T>(result);
    	}

        BRelation<T,S> inverse() {
            immer::set<BCouple<T,S>,typename BRelation<T,S>::Hash, typename BRelation<T,S>::HashEqual> result;
            for(BCouple<S,T> couple : this->set) {
                result = result.insert(BCouple<T,S>(couple.projection2(), couple.projection1()));
            }
            return BRelation<T,S>(result);
        }

        BRelation<S,T> domainRestriction(const BSet<S>& arg) {
            immer::set<BCouple<S,T>,typename BRelation<S,T>::Hash, typename BRelation<S,T>::HashEqual> result;
            for(BCouple<S,T> couple : this->set) {
                if(arg.contains(couple.projection1())) {
                    result = result.insert(couple);
                }
            }
            return BRelation<S,T>(result);
        }

        BRelation<S,T> domainSubstraction(const BSet<S>& arg) {
            immer::set<BCouple<S,T>,typename BRelation<S,T>::Hash, typename BRelation<S,T>::HashEqual> result;
            for(BCouple<S,T> couple : this->set) {
                if(!arg.contains(couple.projection1())) {
                    result = result.insert(couple);
                }
            }
            return BRelation<S,T>(result);
        }

        BRelation<S,T> rangeRestriction(const BSet<T>& arg) {
            immer::set<BCouple<S,T>,typename BRelation<S,T>::Hash, typename BRelation<S,T>::HashEqual> result;
            for(BCouple<S,T> couple : this->set) {
                if(arg.contains(couple.projection2())) {
                    result = result.insert(couple);
                }
            }
            return BRelation<S,T>(result);
        }

        BRelation<S,T> rangeSubstraction(const BSet<T>& arg) {
            immer::set<BCouple<S,T>,typename BRelation<S,T>::Hash, typename BRelation<S,T>::HashEqual> result;
            for(BCouple<S,T> couple : this->set) {
                if(!arg.contains(couple.projection2())) {
                    result = result.insert(couple);
                }
            }
            return BRelation<S,T>(result);
        }

        BRelation<S,T> override(const BRelation<S,T>& arg) {
            return arg._union(this->domainRestriction(arg.domain()));
        }

    	T first() {
    		return functionCall((S) BInteger(1));
    	}

    	T last() {
    		return functionCall((S) card());
    	}

    	BRelation<S,T> reverse() {
    		BInteger size = card();
    		BRelation<S,T> result = BRelation<S,T>();
    		for(BInteger i = BInteger(1); i.lessEqual(size).booleanValue(); i = i.next()) {
    			result = result._union(BRelation<S,T>(BCouple<S,T>((S) i, (T) functionCall((S) size.minus(i).next()))));
    		}
    		return result;
    	}

    	BRelation<S,T> front() {
    		return domainSubstraction(BSet<S>((S) card()));
    	}

    	BRelation<S,T> tail() {
    		BRelation<S,T> result = BRelation<S,T>();
    		BRelation<S,T> couplesWithoutFirst = domainSubstraction(BSet<S>((S) BInteger(1)));
    		for(BCouple<S,T> couple : couplesWithoutFirst) {
    			result = result._union(BRelation<S,T>(BCouple<S,T>((S) ((BInteger) couple.projection1()).previous(), couple.projection2())));
    		}
    		return result;
    	}

    	BRelation<S,T> take(const BInteger& n) {
    		BRelation<S,T> result = BRelation<S,T>();
    		for(BCouple<S,T> couple : this->set) {
    			if(((BInteger) couple.projection1()).lessEqual(n).booleanValue()) {
    				result = result._union(BRelation<S,T>(couple));
    			}
    		}
    		return result;
    	}

    	BRelation<S,T> drop(const BInteger& n) {
    		BRelation<S,T> result = BRelation<S,T>();
    		for(BCouple<S,T> couple : this->set) {
    			if(((BInteger) couple.projection1()).greater(n).booleanValue()) {
    				result = result._union(BRelation<S,T>(couple));
    			}
    		}
    		return result;
    	}

    	BRelation<S,T> concat(const BRelation<S,T>& arg) {
    		BRelation<S,T> result = *this;
    		BInteger size = card();
    		for(BCouple<S,T> couple : arg.set) {
    			result = result._union(BRelation<S,T>(BCouple<S,T>((S) size.plus((BInteger) couple.projection1()), couple.projection2())));
    		}
    		return result;
    	}

    	BRelation<S,T> append(const T& arg) {
    		BInteger size = card();
    		return _union(BRelation<S,T>(BCouple<S,T>((S) size.next(), arg)));
    	}

    	BRelation<S,T> prepend(const T& arg) {
    		BRelation<S,T> result = BRelation<S,T>(BCouple<S,T>((S) BInteger(1), arg));
    		for(BCouple<S,T> couple : this->set) {
    			result = result._union(BRelation<S,T>(BCouple<S,T>((S) ((BInteger) couple.projection1()).next(), couple.projection2())));
    		}
    		return result;
    	}

    	template<typename R>
    	BRelation<S,BCouple<T,R>> directProduct(const BRelation<S,R>& arg) {
    		BRelation<S,BCouple<T,R>> result = BRelation<S,BCouple<T,R>>();
    		for(BCouple<S,T> e1 : this->set) {
    			for(BCouple<S,R> e2 : arg.set) {
    				if(e1.projection1().equal(e2.projection1()).booleanValue()) {
    					result = result._union(BRelation<S, BCouple<T, R>>(BCouple<S, BCouple<T,R>>(e1.projection1(), BCouple<T,R>(e1.projection2(), e2.projection2()))));
    				}
    			}
    		}
    		return result;
    	}

    	template<typename R,typename A>
    	BRelation<BCouple<S,R>,BCouple<T,A>> parallelProduct(const BRelation<R,A>& arg) {
    		BRelation<BCouple<S,R>, BCouple<T,A>> result = BRelation<BCouple<S, R>, BCouple<T, A>>();
    		for(BCouple<S,T> e1 : this->set) {
    			for(BCouple<R,A> e2 : arg.set) {
    				result = result._union(BRelation<BCouple<S, R>, BCouple<T, A>>(BCouple<BCouple<S, R>, BCouple<T, A>>(BCouple<S,R>(e1.projection1(), e2.projection1()), BCouple<T,A>(e1.projection2(), e2.projection2()))));
    			}
    		}
    		return result;
    	}

    	template <typename R>
    	BRelation<S,R> composition(const BRelation<T,R>& arg) {
    		BRelation<S,R> result = BRelation<S,R>();
    		for(BCouple<S,T> e1 : this->set) {
    			for(BCouple <T,R> e2 : arg.set) {
    				if(e1.projection2().equal(e2.projection1()).booleanValue()) {
    					result = result._union(BRelation<S,R>(BCouple<S,R>(e1.projection1(), e2.projection2())));
    				}
    			}
    		}
    		return result;
    	}

    	BRelation<S,S> iterate(BInteger n) {
    		BRelation<S,S> thisRelation = (BRelation<S,S>) *this;
            BRelation<T,T> result;
            for(const T& e : this->domain()) {
                result = result._union(BRelation<T,T>(BCouple<T,T>(e,e)));
            }
    		for(BInteger i = BInteger(1); i.lessEqual(n).booleanValue(); i = i.next()) {
    			result = thisRelation.composition(result);
    		}
    		return result;
    	}

    	BRelation<S,S> closure() {
    		BRelation<S,S> thisRelation = (BRelation<S,S>) *this;
            BRelation<T,T> result;
            for(const T& e : this->domain()) {
                result = result._union(BRelation<T,T>(BCouple<T,T>(e,e)));
            }
    		BRelation<S,S> nextResult = result.composition(thisRelation);
    		while(!result.equal(nextResult).booleanValue()) {
    			result = nextResult;
    			nextResult = result.composition(thisRelation);
    		}
    		return result;
    	}

    	BRelation<S,S> closure1() {
    		BRelation<S,S> thisRelation = (BRelation<S,S>) *this;
    		BRelation<S,S> result = (BRelation<S,S>) *this;
    		BRelation<S,S> nextResult = result.composition(thisRelation);
    		while(!result.equal(nextResult).booleanValue()) {
    			result = nextResult;
    			nextResult = result.composition(thisRelation);
    		}
    		return result;
    	}

        static BRelation<BCouple<S,T>, S> projection1(const BSet<S>& arg1, const BSet<T>& arg2) {
            BRelation<BCouple<S,T>, S> result = BRelation<BCouple<S,T>, S>();
            for(S e1 : arg1.getSet()) {
                for(T e2 : arg2.getSet()) {
                    result = result._union(BRelation<BCouple<S,T>, S>(BCouple<BCouple<S,T>, S>(BCouple<S,T>(e1,e2), e1)));
                }
            }
            return result;
        }

    	static BRelation<BCouple<S,T>, T> projection2(const BSet<S>& arg1, const BSet<T>& arg2) {
    		BRelation<BCouple<S,T>, T> result = BRelation<BCouple<S,T>, T>();
    		for(S e1 : arg1.getSet()) {
    			for(T e2 : arg2.getSet()) {
    				result = result._union(BRelation<BCouple<S,T>, T>(BCouple<BCouple<S,T>, T>(BCouple<S,T>(e1,e2), e2)));
    			}
    		}
    		return result;
    	}

        void operator =(const BRelation<S,T>& other) {
            this->set = other.set;
        }

        int hashCode() const {
            return 0;
        }

        protected:
            immer::set<BCouple<S,T>,Hash, HashEqual> set;

};

        /*template<typename T>
        BRelation<T,T> BSet<BCouple<T,T>>::identity() {
            BRelation<T,T> result;
            for(const T& e : set) {
                result = result._union(BRelation<T,T>(BCouple<T,T>(e,e)));
            }
            return result;
        }*/

#endif