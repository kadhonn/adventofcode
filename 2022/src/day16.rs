use std::collections::{HashMap, HashSet};
use std::hash::{Hash};
use regex::Regex;

pub fn day16_1(str: &str) {
    let valves = parse_input(str);

    let mut visited_states: HashMap<StateHash, i32> = HashMap::new();

    let mut current_states = vec![State {
        current_position: "AA",
        current_release: 0,
        total_release: 0,
        opened_valves: HashSet::new(),
    }];

    for _ in 0..30 {
        println!("{} {}", current_states.len(), visited_states.len());
        let mut next_states = vec![];
        // println!();
        // println!("current:");
        // for state in &current_states {
        //     println!("{:?}", state);
        // }
        // println!("visited:");
        // for state in &visited_states {
        //     println!("{:?}", state);
        // }
        for state in &current_states {
            // let (hash, total_release) = state.to_hashable();
            // if has_better(&visited_states, &hash, total_release) {
            //     println!("had better: {:?}", state);
            // continue;
            // }
            // visited_states.insert(hash, total_release);

            for next_state in get_next_states(&valves, state) {
                let (hash, total_release) = next_state.to_hashable();
                if !has_better(&visited_states, &hash, total_release) {
                    next_states.push(next_state);
                    visited_states.insert(hash, total_release);
                }
            }
        }

        current_states = next_states;
    }

    // println!("fu");
    // for state in &current_states {
    //     println!("{}", state.total_release);
    // }

    let mut max = 0;
    for state in &current_states {
        max = max.max(state.total_release);
    }

    println!("{max}");
}

fn get_next_states<'a>(valves: &'a HashMap<String, Valve>, state: &State<'a>) -> Vec<State<'a>> {
    let mut next_states = vec![];

    let next_total_release = state.total_release + state.current_release;
    if state.opened_valves.len() != valves.len() {
        if !state.opened_valves.contains(state.current_position) {
            let mut next_opened_valves = state.opened_valves.clone();
            next_opened_valves.insert(state.current_position);
            next_states.push(State {
                current_position: state.current_position,
                current_release: state.current_release + valves.get(state.current_position).unwrap().rate,
                total_release: next_total_release,
                opened_valves: next_opened_valves,
            });
        }
        for lead_to in &valves.get(state.current_position).unwrap().leads_to {
            next_states.push(State {
                current_position: lead_to,
                current_release: state.current_release,
                total_release: next_total_release,
                opened_valves: state.opened_valves.clone(),
            });
        }
    }

    next_states.push(State {
        current_position: state.current_position,
        current_release: state.current_release,
        total_release: next_total_release,
        opened_valves: state.opened_valves.clone(),
    });

    next_states
}

fn has_better(visited_states: &HashMap<StateHash, i32>, hash: &StateHash, total_release: i32) -> bool {
    let result = visited_states.get(hash);
    match result {
        None => false,
        Some(existing_total_release) => *existing_total_release >= total_release
    }
}

impl<'a> State<'a> {
    fn to_hashable(&self) -> (StateHash, i32) {
        let mut opened_valves: Vec<String> = self.opened_valves.iter().map(|a| { a.to_string() }).collect();
        opened_valves.sort();
        (
            StateHash {
                current_position: self.current_position.to_string(),
                opened_valves,
            },
            self.total_release
        )
    }
}

#[derive(Eq, Hash, PartialEq, Debug)]
struct StateHash {
    current_position: String,
    opened_valves: Vec<String>,
}

#[derive(Debug)]
struct State<'a> {
    current_position: &'a str,
    current_release: i32,
    total_release: i32,
    opened_valves: HashSet<&'a str>,
}

fn fix_all_ways(valves: &mut HashMap<String, Valve>) {
    let names: Vec<String> = valves.iter().map(|a| { a.0.to_string() }).collect();
    for name in names {
        let valve = valves.get(&name).unwrap().leads_to.clone();
        for connected in valve {
            valves.get_mut(&connected).unwrap().leads_to.insert(name.to_string());
        }
    }
}

struct Valve {
    name: String,
    rate: i32,
    leads_to: HashSet<String>,
}

fn parse_input(str: &str) -> HashMap<String, Valve> {
    let regex = Regex::new(r"^Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.+)$").unwrap();

    let mut valves = HashMap::new();

    for line in str.lines() {
        if line.is_empty() {
            continue;
        }
        let valve = regex.captures_iter(line).next().unwrap();
        valves.insert(valve[1].to_string(), Valve {
            name: valve[1].to_string(),
            rate: valve[2].parse().unwrap(),
            leads_to: valve[3].split(", ").map(|s| { s.to_string() }).collect(),
        });
    }
    fix_all_ways(&mut valves);

    valves
}
