use std::cell::RefCell;
use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::f32::consts::E;
use std::i64::MAX;
use std::io::BufRead;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use priority_queue::PriorityQueue;
use regex::Regex;
use crate::day20::Type::{BROADCASTER, CONJUNCTION, FLIPFLOP};


#[derive(Clone)]
struct Module<'a> {
    name: &'a str,
    module_type: Type,
    next: Vec<&'a str>,
    from: Vec<&'a str>,
}

#[derive(PartialEq, Copy, Clone)]
enum Type {
    BROADCASTER,
    FLIPFLOP,
    CONJUNCTION,
}

pub fn day20(str: &str) {
    let mut all_modules = HashMap::new();

    for line in str.lines() {
        let split: Vec<&str> = line.split(" -> ").collect();
        let full_name = split[0];
        let next_string = split[1];
        let (module_type, name) = if full_name.starts_with("%") {
            (FLIPFLOP, &full_name[1..])
        } else if full_name.starts_with("&") {
            (CONJUNCTION, &full_name[1..])
        } else {
            (BROADCASTER, full_name)
        };
        let next: Vec<&str> = next_string.split(", ").collect();
        let from = vec![];
        let module = Module {
            name,
            module_type,
            next,
            from,
        };
        all_modules.insert(module.name, module);
    }

    for module in all_modules.clone().values() {
        for next in &module.next {
            if all_modules.contains_key(next) {
                all_modules.get_mut(next).unwrap().from.push(module.name);
            }
        }
    }

    let mut flip_flip_memory = HashMap::new();
    let mut conjunction_memory = HashMap::new();
    for (_, module) in &all_modules {
        if module.module_type == FLIPFLOP {
            flip_flip_memory.insert(module.name, false);
        } else if module.module_type == CONJUNCTION {
            let mut inputs = HashMap::new();
            for input in &module.from {
                inputs.insert(input.to_string(), false);
            }
            conjunction_memory.insert(module.name, inputs);
        }
    }

    let mut low_count = 0;
    let mut high_count = 0;
    for _ in 0..1000 {
        let mut queue = LinkedList::new();
        queue.push_back(("broadcaster", "button", false));

        while !queue.is_empty() {
            let (dest, from, high) = queue.pop_front().unwrap();
            if high {
                high_count += 1;
            } else {
                low_count += 1;
            }
            if let Some(module) = all_modules.get(dest) {
                if module.module_type == BROADCASTER {
                    for next in &module.next {
                        queue.push_back((next, &module.name, high));
                    }
                } else if module.module_type == FLIPFLOP {
                    if !high {
                        let mut state = flip_flip_memory[module.name];
                        state = !state;
                        flip_flip_memory.insert(module.name, state);
                        for next in &module.next {
                            queue.push_back((next, &module.name, state));
                        }
                    }
                } else if module.module_type == CONJUNCTION {
                    let mut state = conjunction_memory.get_mut(module.name).unwrap();
                    state.insert(from.to_string(), high);
                    let output = state.iter().any(|(_, input)| *input == false);
                    for next in &module.next {
                        queue.push_back((next, &module.name, output));
                    }
                } else {
                    panic!("wut");
                }
            }
        }
    }

    println!("{}", high_count * low_count);
}