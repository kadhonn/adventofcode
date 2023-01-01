use priority_queue::PriorityQueue;
use crate::day24::State::{Blizzard, Nothing};

pub fn day24_1(str: &str) {
    let (start, end, field) = parse_field(str);

    let mut cache = vec![field];
    let start_state = (0, start);

    let mut queue = PriorityQueue::new();
    queue.push(start_state, get_priority(start_state, end));


    loop {
        let state = queue.pop().expect("oh no, queue is empty").0;
        if state.1.0 == end.0 && state.1.1 == end.1 {
            println!("found exit after {} minutes", state.0);
            break;
        }

        let next_states = get_next_states(state, &mut cache);

        for next_state in next_states {
            queue.push(next_state, get_priority(next_state, end));
        }
    }
}

fn get_next_states(state: (i32, (i32, i32)), cache: &mut Vec<Vec<Vec<State>>>) -> Vec<(i32, (i32, i32))> {
    let mut result = vec![];

    let next_field = get_field(state.0 + 1, cache);

    for dir in [
        (0, 0),
        (1, 0),
        (-1, 0),
        (0, 1),
        (0, -1),
    ] {
        let new_pos = (state.1.0 + dir.0, state.1.1 + dir.1);
        if new_pos.0 >= 0 && new_pos.0 <= next_field[0].len() as i32 - 1 && new_pos.1 >= 0 && new_pos.1 <= next_field.len() as i32 - 1 {
            if next_field[new_pos.1 as usize][new_pos.0 as usize] == Nothing {
                result.push((state.0 + 1, new_pos));
            }
        }
    }

    result
}

fn get_field(round: i32, cache: &mut Vec<Vec<Vec<State>>>) -> &Vec<Vec<State>> {
    for i in (cache.len() as i32 - 1)..round {
        let old_field = &cache[i as usize];
        let new_field = move_field(old_field);
        cache.push(new_field);
    }

    return &cache[round as usize];
}

fn move_field(old_field: &Vec<Vec<State>>) -> Vec<Vec<State>> {
    let mut new_field = vec![vec![Nothing; old_field[0].len()]; old_field.len()];

    for y in 0..old_field.len() {
        for x in 0..old_field[y].len() {
            let old_state = old_field[y][x];
            match old_state {
                State::Nothing => {
                    //nothing
                }
                State::Wall => {
                    new_field[y][x] = State::Wall;
                }
                State::Blizzard(up, down, left, right) => {
                    if up {
                        let new_pos = (x as i32, y as i32 - 1);
                        let new_pos = fix_pos(new_pos, old_field);
                        add_blizzard(true, false, false, false, new_pos, &mut new_field);
                    }
                    if down {
                        let new_pos = (x as i32, y as i32 + 1);
                        let new_pos = fix_pos(new_pos, old_field);
                        add_blizzard(false, true, false, false, new_pos, &mut new_field);
                    }
                    if left {
                        let new_pos = (x as i32 - 1, y as i32);
                        let new_pos = fix_pos(new_pos, old_field);
                        add_blizzard(false, false, true, false, new_pos, &mut new_field);
                    }
                    if right {
                        let new_pos = (x as i32 + 1, y as i32);
                        let new_pos = fix_pos(new_pos, old_field);
                        add_blizzard(false, false, false, true, new_pos, &mut new_field);
                    }
                }
            }
        }
    }

    new_field
}

fn add_blizzard(up: bool, down: bool, left: bool, right: bool, pos: (i32, i32), field: &mut Vec<Vec<State>>) {
    if let Nothing = field[pos.1 as usize][pos.0 as usize] {
        field[pos.1 as usize][pos.0 as usize] = Blizzard(up, down, left, right);
    } else if let Blizzard(old_up, old_down, old_left, old_right) = field[pos.1 as usize][pos.0 as usize] {
        field[pos.1 as usize][pos.0 as usize] = Blizzard(up || old_up, down || old_down, left || old_left, right || old_right);
    }
}

fn fix_pos(pos: (i32, i32), field: &Vec<Vec<State>>) -> (i32, i32) {
    let mut x = pos.0;
    let mut y = pos.1;

    if x == 0 {
        x = field[0].len() as i32 - 2;
    }
    if x == field[0].len() as i32 - 1 {
        x = 1;
    }
    if y == 0 {
        y = field.len() as i32 - 2;
    }
    if y == field.len() as i32 - 1 {
        y = 1;
    }

    (x, y)
}

fn get_priority(state: (i32, (i32, i32)), end: (i32, i32)) -> i32 {
    return -(state.0 + (end.0 - state.1.0).abs() + (end.1 - state.1.1).abs());
}

fn parse_field(str: &str) -> ((i32, i32), (i32, i32), Vec<Vec<State>>) {
    let mut field = vec!();

    for line in str.lines() {
        if line.is_empty() {
            continue;
        }
        let mut field_line = vec!();
        for c in line.chars() {
            let state = match c {
                '#' => State::Wall,
                '.' => State::Nothing,
                '^' => State::Blizzard(true, false, false, false),
                'v' => State::Blizzard(false, true, false, false),
                '<' => State::Blizzard(false, false, true, false),
                '>' => State::Blizzard(false, false, false, true),
                wat => panic!("unknown char {wat}")
            };
            field_line.push(state);
        }
        field.push(field_line);
    }

    let mut start = (0, 0);
    let mut end = (0, 0);
    for i in 0..field[0].len() {
        if field[0][i] == Nothing {
            start = (i as i32, 0);
        }
        if field[field.len() - 1][i] == Nothing {
            end = (i as i32, field.len() as i32 - 1);
        }
    }

    (start, end, field)
}

#[derive(Eq, PartialEq, Copy, Clone)]
enum State {
    Nothing,
    Wall,
    Blizzard(bool, bool, bool, bool), //up, down, left, right
}