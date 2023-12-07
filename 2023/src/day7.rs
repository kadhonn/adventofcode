use std::cmp::{max, min, Ordering};
use std::collections::HashMap;
use std::i64::MAX;
use std::ops::{Index, Shl};

struct Hand<'a> {
    cards: &'a str,
    bid: i32,
}

impl<'a> Hand<'a> {
    fn hand_type(&self) -> i32 {
        let groups = self.group();
        return groups.iter().map(|group| {
            if self.is_five_of_a_kind(group) {
                return 7;
            }
            if self.is_four_of_a_kind(group) {
                return 6;
            }
            if self.is_full_house(group) {
                return 5;
            }
            if self.is_three_of_a_kind(group) {
                return 4;
            }
            if self.is_two_pair(group) {
                return 3;
            }
            if self.is_pair(group) {
                return 2;
            }
            return 1;
        }).max().unwrap();
    }
    fn is_five_of_a_kind(&self, group: &HashMap<char, i32>) -> bool {
        return group.iter().any(|entry| *entry.1 == 5);
    }
    fn is_four_of_a_kind(&self, group: &HashMap<char, i32>) -> bool {
        return group.iter().any(|entry| *entry.1 == 4);
    }
    fn is_full_house(&self, group: &HashMap<char, i32>) -> bool {
        return group.iter().any(|entry| *entry.1 == 3) && group.iter().any(|entry| *entry.1 == 2);
    }
    fn is_three_of_a_kind(&self, group: &HashMap<char, i32>) -> bool {
        return group.iter().any(|entry| *entry.1 == 3);
    }
    fn is_two_pair(&self, group: &HashMap<char, i32>) -> bool {
        return group.iter().filter(|entry| *entry.1 == 2).count() == 2;
    }
    fn is_pair(&self, group: &HashMap<char, i32>) -> bool {
        return group.iter().filter(|entry| *entry.1 == 2).count() == 1;
    }
    fn group(&self) -> Vec<HashMap<char, i32>> {
        let mut map = HashMap::new();
        for char in self.cards.chars() {
            if map.contains_key(&char) {
                map.insert(char, map[&char] + 1);
            } else {
                map.insert(char, 1);
            }
        }
        if !map.contains_key(&'J') {
            return vec![map];
        }

        let mut maps = vec![];
        maps.push(map.clone());
        let jokers = map.remove(&'J').unwrap();
        let keys = map.keys().map(|k| *k).collect::<Vec<char>>();
        add_jokers(&mut maps, &keys, jokers, &map);

        return maps;
    }
}

fn add_jokers(maps: &mut Vec<HashMap<char, i32>>, keys: &Vec<char>, jokers: i32, map: &HashMap<char, i32>) {
    if jokers == 0 {
        maps.push(map.clone());
        return;
    }
    for key in keys {
        let mut map = map.clone();
        map.insert(*key, map[key] + 1);
        add_jokers(maps, keys, jokers - 1, &map);
    }
}


pub fn day7(str: &str) {
    let mut card_ranking = vec!['A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'];
    card_ranking.reverse();
    let mut hands = str.lines().map(|line| {
        let split = line.split(" ").collect::<Vec<&str>>();
        Hand {
            cards: split[0],
            bid: split[1].parse().unwrap(),
        }
    }).collect::<Vec<Hand>>();

    hands.sort_by(|hand1, hand2| {
        let type1 = hand1.hand_type();
        let type2 = hand2.hand_type();
        if type1 == type2 {
            for i in 0..hand1.cards.len() {
                let card1 = hand1.cards.chars().collect::<Vec<char>>()[i];
                let card2 = hand2.cards.chars().collect::<Vec<char>>()[i];
                let card1_value = card_ranking.iter().position(|e| card1 == *e);
                let card2_value = card_ranking.iter().position(|e| card2 == *e);
                if card1_value < card2_value {
                    return Ordering::Less;
                } else if card1_value > card2_value {
                    return Ordering::Greater;
                }
            }
            return Ordering::Equal;
        } else if type1 < type2 {
            return Ordering::Less;
        } else {
            return Ordering::Greater;
        }
    });

    let mut sum = 0;
    for i in 0..hands.len() {
        sum += hands[i].bid * (i as i32 + 1);
    }

    println!("{}", sum);
}
