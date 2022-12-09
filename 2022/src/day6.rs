use std::collections::{HashSet, LinkedList};

pub fn day6_1(str: &str) {
    let chars: Vec<char> = str.chars().collect();
    let mut last_three: LinkedList<char> = LinkedList::new();

    for j in 0..13 {
        last_three.push_back(chars[j]);
    }

    let mut i = 13;

    loop {
        last_three.push_back(chars[i]);
        let set: HashSet<char> = HashSet::from_iter(last_three.iter().cloned());
        if set.len() == 14 {
            break;
        }
        last_three.pop_front();
        i += 1;
    }

    println!("{}", i+1);
}