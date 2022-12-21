use std::collections::{HashMap, HashSet};
use regex::Regex;

pub fn day16_2(str: &str) {
    let valves = parse_input(str);

    let mut current_states = vec![State {
        current_position_me: "AA",
        current_position_el: "AA",
        current_release: 0,
        total_release: 0,
        opened_valves: HashSet::new(),
    }];

    for _ in 0..26 {
        println!("{}", current_states.len());
        let mut next_states = vec![];
        // println!();
        // println!("current:");
        // for state in &current_states {
        //     println!("{:?}", state);
        // }
        for state in &current_states {
            for next_state in get_next_states(&valves, state) {
                next_states.push(next_state);
            }
        }

        current_states = prune(next_states);
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

fn prune(states: Vec<State>) -> Vec<State> {
    let mut best_states = HashMap::new();

    'outer: for state in &states {
        let state_position = state.current_position_me.to_string() + state.current_position_el;
        if !best_states.contains_key(&state_position) {
            best_states.insert(state_position, vec![state]);
        } else {
            let best_states = best_states.get_mut(&state_position).unwrap();
            for best_state in best_states.iter() {
                if state.total_release <= best_state.total_release && state.current_release <= best_state.current_release {
                    continue 'outer;
                }
            }
            best_states.push(&state);
        }
    }

    let mut result = vec![];
    for (_str, states) in &best_states {
        'outer: for (i, state) in states.iter().enumerate() {
            for (j, best_state) in states.iter().enumerate() {
                if i != j && state.total_release <= best_state.total_release && state.current_release <= best_state.current_release {
                    continue 'outer;
                }
            }

            result.push((*state).clone());
        }
    }

    result
}

fn get_next_states<'a>(valves: &'a HashMap<String, Valve>, state: &State<'a>) -> Vec<State<'a>> {
    let mut next_states_me = vec![];

    let next_total_release = state.total_release + state.current_release;
    if state.opened_valves.len() != valves.len() {
        if !state.opened_valves.contains(state.current_position_me) {
            let mut next_opened_valves = state.opened_valves.clone();
            next_opened_valves.insert(state.current_position_me);
            next_states_me.push(State {
                current_position_me: state.current_position_me,
                current_position_el: state.current_position_el,
                current_release: state.current_release + valves.get(state.current_position_me).unwrap().rate,
                total_release: next_total_release,
                opened_valves: next_opened_valves,
            });
        }
        for lead_to in &valves.get(state.current_position_me).unwrap().leads_to {
            next_states_me.push(State {
                current_position_me: lead_to,
                current_position_el: state.current_position_el,
                current_release: state.current_release,
                total_release: next_total_release,
                opened_valves: state.opened_valves.clone(),
            });
        }
    } else {
        next_states_me.push(State {
            current_position_me: state.current_position_me,
            current_position_el: state.current_position_el,
            current_release: state.current_release,
            total_release: next_total_release,
            opened_valves: state.opened_valves.clone(),
        });
    }

    let mut next_states_el = vec![];
    for state in next_states_me {
        if state.opened_valves.len() != valves.len() {
            if !state.opened_valves.contains(state.current_position_el) {
                let mut next_opened_valves = state.opened_valves.clone();
                next_opened_valves.insert(state.current_position_el);
                next_states_el.push(State {
                    current_position_me: state.current_position_me,
                    current_position_el: state.current_position_el,
                    current_release: state.current_release + valves.get(state.current_position_el).unwrap().rate,
                    total_release: next_total_release,
                    opened_valves: next_opened_valves,
                });
            }
            for lead_to in &valves.get(state.current_position_el).unwrap().leads_to {
                next_states_el.push(State {
                    current_position_me: state.current_position_me,
                    current_position_el: lead_to,
                    current_release: state.current_release,
                    total_release: next_total_release,
                    opened_valves: state.opened_valves.clone(),
                });
            }
        } else {
            next_states_el.push(State {
                current_position_me: state.current_position_me,
                current_position_el: state.current_position_el,
                current_release: state.current_release,
                total_release: next_total_release,
                opened_valves: state.opened_valves.clone(),
            });
        }
    }

    next_states_el
}

#[derive(Debug, Clone)]
struct State<'a> {
    current_position_me: &'a str,
    current_position_el: &'a str,
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
