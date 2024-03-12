import json
import random
import functools


def make_activity(id, sectors):
    return {
        "id": id,
        "title": "Activity Title",
        "description": "Activity description",
        "matchesFilters": {
            "org": sectors,
        },
    }


data = json.load(open('data/structures/wb-data.json'))
activity_ids = set(f['properties']['projectId'] for f in data['features'])

org_data = json.load(open('data/filters/orgs.json'))
org_ids = [f['id'] for f in org_data]

num_orgs_distribution = [int(5**(n/4) // 1) + 1 for n in range(-16, 5)]
org_weights = [int(1.4**(n/3) // 1) for n in range(0, 24)]
weighted_org_ids = functools.reduce(lambda a, b: a + b,
    ([o_id for _ in range(1, random.choice(org_weights)+1)] for o_id in org_ids))


activities = [
    make_activity(a_id,
        list(
            set(
                random.sample(
                    weighted_org_ids,
                    random.choice(
                        num_orgs_distribution )))))
    for a_id in activity_ids ]


json.dump(activities, open('data/activities/list.json', 'w'), indent=2)

for activity in activities:
    json.dump(
        activity,
        open('data/activities/{id}.json'.format(**activity), 'w'),
        indent=2 )
    print("    'GET /rest/gis/activities/{id}': "
          "fs.readFileSync(__dirname + '/data/activities/{id}.json', 'utf8'),"
            .format(**activity))
