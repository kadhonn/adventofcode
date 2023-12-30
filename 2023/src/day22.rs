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


pub fn day22(str: &str) {
    let mut bricks = vec![];
    let mut max_x = 0;
    let mut max_y = 0;
    let mut max_z = 0;
    for line in str.lines() {
        let mut split = line.split('~');
        let mut from_split = split.next().unwrap().split(',').map(|str| str.parse::<i32>().unwrap());
        let mut to_split = split.next().unwrap().split(',').map(|str| str.parse::<i32>().unwrap());
        let from = (from_split.next().unwrap(), from_split.next().unwrap(), from_split.next().unwrap());
        let to = (to_split.next().unwrap(), to_split.next().unwrap(), to_split.next().unwrap());
        let is_less = from.lt(&to);
        bricks.push(Brick {
            number: bricks.len() as i32,
            from: if is_less { from } else { to },
            to: if is_less { to } else { from },
        });
        max_x = i32::max(max_x, i32::max(from.0, to.0));
        max_y = i32::max(max_y, i32::max(from.1, to.1));
        max_z = i32::max(max_z, i32::max(from.2, to.2));
    }

    let mut map = HashMap::new();
    for brick in &bricks {
        brick.add(&mut map);
    }

    for z in 0..=max_z {
        for brick in &mut bricks {
            if brick.from.2 == z {
                brick.remove(&mut map);
                while brick.from.2 > 0 {
                    brick.move_down();
                    if brick.intersects(&mut map) {
                        brick.move_up();
                        break;
                    }
                }
                brick.add(&mut map);
            }
        }
    }

    let supports: Vec<HashSet<i32>> = bricks.iter_mut().map(|brick| {
        brick.remove(&mut map);
        brick.move_up();
        let supports = brick.intersections(&map);
        brick.move_down();
        brick.add(&mut map);
        supports
    }).collect();

    let is_supported_by: Vec<HashSet<i32>> = bricks.iter_mut().map(|brick| {
        brick.remove(&mut map);
        brick.move_down();
        let supports = brick.intersections(&map);
        brick.move_up();
        brick.add(&mut map);
        supports
    }).collect();

    let mut count = 0;
    for i in 0..supports.len() {
        let mut disintegrated = HashSet::new();
        let mut to_disintegrate = LinkedList::new();
        to_disintegrate.push_back(i as i32);
        while !to_disintegrate.is_empty() {
            let current = to_disintegrate.pop_front().unwrap();
            disintegrated.insert(current);
            for upper in &supports[current as usize] {
                if is_supported_by[*upper as usize].difference(&disintegrated).count() == 0 {
                    count += 1;
                    to_disintegrate.push_back(*upper);
                }
            }
        }
    }

    println!("{count}");
    // print_map_x(max_x, max_y, max_z, &mut map);
    // println!();
    // println!();
    // print_map_y(max_x, max_y, max_z, &mut map);
}

fn print_map_x(max_x: i32, max_y: i32, max_z: i32, map: &mut HashMap<(i32, i32, i32), i32>) {
    for z in (0..=max_z).rev() {
        for x in 0..=max_x {
            let mut toprint = ' ';
            for y in 0..=max_y {
                if map.contains_key(&(x, y, z)) {
                    let new = ((map[&(x, y, z)] as u8) + ('A' as u8)) as char;
                    if toprint == ' ' || toprint == new {
                        toprint = new;
                    } else {
                        toprint = '?';
                    }
                }
            }
            print!("{toprint}");
        }
        println!();
    }
}

fn print_map_y(max_x: i32, max_y: i32, max_z: i32, map: &mut HashMap<(i32, i32, i32), i32>) {
    for z in (0..=max_z).rev() {
        for y in 0..=max_y {
            let mut toprint = ' ';
            for x in 0..=max_x {
                if map.contains_key(&(x, y, z)) {
                    let new = ((map[&(x, y, z)] as u8) + ('A' as u8)) as char;
                    if toprint == ' ' || toprint == new {
                        toprint = new;
                    } else {
                        toprint = '?';
                    }
                }
            }
            print!("{toprint}");
        }
        println!();
    }
}

struct Brick {
    number: i32,
    from: (i32, i32, i32),
    to: (i32, i32, i32),
}

impl Brick {
    fn cubes(&self) -> Vec<(i32, i32, i32)> {
        let mut cubes = vec![];

        for x in self.from.0..=self.to.0 {
            for y in self.from.1..=self.to.1 {
                for z in self.from.2..=self.to.2 {
                    cubes.push((x, y, z));
                }
            }
        }

        return cubes;
    }

    fn remove(&self, map: &mut HashMap<(i32, i32, i32), i32>) {
        for cube in self.cubes() {
            map.remove(&cube);
        }
    }

    fn add(&self, map: &mut HashMap<(i32, i32, i32), i32>) {
        for cube in self.cubes() {
            map.insert(cube, self.number);
        }
    }

    fn intersects(&self, map: &HashMap<(i32, i32, i32), i32>) -> bool {
        for cube in self.cubes() {
            if map.contains_key(&cube) {
                return true;
            }
        }
        return false;
    }
    fn intersections(&self, map: &HashMap<(i32, i32, i32), i32>) -> HashSet<i32> {
        let mut intersections = HashSet::new();
        for cube in self.cubes() {
            if map.contains_key(&cube) {
                intersections.insert(map[&cube]);
            }
        }
        return intersections;
    }

    fn move_up(&mut self) {
        self.from.2 += 1;
        self.to.2 += 1;
    }

    fn move_down(&mut self) {
        self.from.2 -= 1;
        self.to.2 -= 1;
    }
}
